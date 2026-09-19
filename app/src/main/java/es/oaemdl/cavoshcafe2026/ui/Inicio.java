package es.oaemdl.cavoshcafe2026.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import es.oaemdl.cavoshcafe2026.controller.ProductoController;
import es.oaemdl.cavoshcafe2026.data.api.ApiClient;
import es.oaemdl.cavoshcafe2026.data.model.ApiResponse;
import es.oaemdl.cavoshcafe2026.data.model.Producto;
import es.oaemdl.cavoshcafe2026.data.pref.SessionManager;
import es.oaemdl.cavoshcafe2026.databinding.FragmentInicioBinding;
import es.oaemdl.cavoshcafe2026.ui.adapter.ProductoAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class Inicio extends Fragment {
    private FragmentInicioBinding binding;
    private SessionManager session;
    private ProductoAdapter nuevosAdapter;
    private ProductoAdapter frecuentesAdapter;
    private final ProductoController controller = new ProductoController();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        session = new SessionManager(requireContext());

        binding.tvUsuario.setText("Good morning, " + session.getUserName());
        binding.tvCafe.setText(session.getSelectedCafeName() + " " + session.getSelectedCafeAddress());

        binding.tvCambiarSede.setOnClickListener(v -> {
            ChooseCafeBottomSheet sheet = new ChooseCafeBottomSheet();
            sheet.setOnCafeSavedListener(cafe -> {
                binding.tvCafe.setText(cafe.getNombre() + " " + cafe.getDireccion());
            });
            sheet.show(getChildFragmentManager(), "choose_cafe");
        });

        nuevosAdapter = new ProductoAdapter(ProductoAdapter.TYPE_VERTICAL);
        nuevosAdapter.setOnProductClickListener(this::abrirDetalleProducto);
        nuevosAdapter.setOnProductAddListener(this::abrirDetalleProducto);
        binding.rvNuevos.setAdapter(nuevosAdapter);

        frecuentesAdapter = new ProductoAdapter(ProductoAdapter.TYPE_HORIZONTAL);
        frecuentesAdapter.setOnProductClickListener(this::abrirDetalleProducto);
        frecuentesAdapter.setOnProductAddListener(this::abrirDetalleProducto);
        binding.rvOrdenados.setAdapter(frecuentesAdapter);

        binding.svBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarProductos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    cargarProductos();
                }
                return false;
            }
        });

        cargarProductos();
    }

    private void cargarProductos() {
        controller.obtenerNuevos(requireContext(), new ProductoController.ProductosCallback() {
            @Override
            public void onProductos(List<Producto> productos) {
                if (isAdded()) nuevosAdapter.submitList(productos);
            }
            @Override
            public void onError(String mensaje) { /* silencioso, el usuario no ve error */ }
        });

        controller.obtenerFrecuentes(requireContext(), new ProductoController.ProductosCallback() {
            @Override
            public void onProductos(List<Producto> productos) {
                if (isAdded()) frecuentesAdapter.submitList(productos);
            }
            @Override
            public void onError(String mensaje) { }
        });
    }

    private void buscarProductos(String q) {
        // Búsqueda siempre va directo a la API (no se cachea)
        ApiClient.getService().buscarProductos(q).enqueue(new Callback<ApiResponse<List<Producto>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Producto>>> call, @NonNull Response<ApiResponse<List<Producto>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    frecuentesAdapter.submitList(response.body().getData());
                }
            }
            @Override public void onFailure(@NonNull Call<ApiResponse<List<Producto>>> call, @NonNull Throwable t) {}
        });
    }

    private void abrirDetalleProducto(Producto p) {
        ProductDetailBottomSheet sheet = ProductDetailBottomSheet.newInstance(p);
        sheet.show(getChildFragmentManager(), "product_detail");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
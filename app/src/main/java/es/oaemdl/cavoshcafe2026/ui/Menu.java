package es.oaemdl.cavoshcafe2026.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import es.oaemdl.cavoshcafe2026.data.api.ApiClient;
import es.oaemdl.cavoshcafe2026.data.model.ApiResponse;
import es.oaemdl.cavoshcafe2026.data.model.Producto;
import es.oaemdl.cavoshcafe2026.data.pref.SessionManager;
import es.oaemdl.cavoshcafe2026.databinding.FragmentMenuBinding;
import es.oaemdl.cavoshcafe2026.ui.adapter.CategoriaAdapter;
import es.oaemdl.cavoshcafe2026.ui.adapter.ProductoAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Arrays;
import java.util.List;

public class Menu extends Fragment {
    private FragmentMenuBinding binding;
    private SessionManager session;
    private ProductoAdapter productoAdapter;
    private String categoriaActual = "Todos";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        session = new SessionManager(requireContext());

        // Configuración de Categorías
        List<String> categorias = Arrays.asList("Todos", "Hot drinks", "Cold drinks", "Bakery");
        CategoriaAdapter categoriaAdapter = new CategoriaAdapter(categorias, cat -> {
            categoriaActual = cat;
            cargarProductosPorCategoria(cat);
        });
        binding.rvCategories.setAdapter(categoriaAdapter);

        // Configuración de Productos Grid
        productoAdapter = new ProductoAdapter(ProductoAdapter.TYPE_GRID);
        productoAdapter.setOnProductClickListener(this::abrirDetalleProducto);
        productoAdapter.setOnProductAddListener(this::abrirDetalleProducto);
        productoAdapter.setOnProductFavoriteListener(this::toggleFavorito);
        binding.tvOpciones.setAdapter(productoAdapter);

        // Búsqueda
        binding.svBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarProductos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    cargarProductosPorCategoria(categoriaActual);
                }
                return false;
            }
        });

        cargarProductosPorCategoria("Todos");
    }

    private void cargarProductosPorCategoria(String categoria) {
        ApiClient.getService().listarProductos(categoria).enqueue(new Callback<ApiResponse<List<Producto>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Producto>>> call, @NonNull Response<ApiResponse<List<Producto>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body().getData();
                    productoAdapter.submitList(productos);
                }
            }
            @Override public void onFailure(@NonNull Call<ApiResponse<List<Producto>>> call, @NonNull Throwable t) {}
        });
    }

    private void buscarProductos(String q) {
        ApiClient.getService().buscarProductos(q).enqueue(new Callback<ApiResponse<List<Producto>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Producto>>> call, @NonNull Response<ApiResponse<List<Producto>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    productoAdapter.submitList(response.body().getData());
                }
            }
            @Override public void onFailure(@NonNull Call<ApiResponse<List<Producto>>> call, @NonNull Throwable t) {}
        });
    }

    private void toggleFavorito(Producto producto, int position) {
        ApiClient.getService().toggleFavorito(session.getUserId(), producto.getIdProducto()).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Boolean>> call, @NonNull Response<ApiResponse<Boolean>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    boolean esFav = Boolean.TRUE.equals(response.body().getData());
                    producto.setFavorito(esFav);
                    productoAdapter.notifyItemChanged(position);
                    Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<ApiResponse<Boolean>> call, @NonNull Throwable t) {}
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
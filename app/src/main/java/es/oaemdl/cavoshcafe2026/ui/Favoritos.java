package es.oaemdl.cavoshcafe2026.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import es.oaemdl.cavoshcafe2026.data.api.ApiClient;
import es.oaemdl.cavoshcafe2026.data.model.ApiResponse;
import es.oaemdl.cavoshcafe2026.data.model.Producto;
import es.oaemdl.cavoshcafe2026.data.pref.SessionManager;
import es.oaemdl.cavoshcafe2026.databinding.FragmentFavoritosBinding;
import es.oaemdl.cavoshcafe2026.ui.adapter.ProductoAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class Favoritos extends Fragment {
    private FragmentFavoritosBinding binding;
    private SessionManager session;
    private ProductoAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        session = new SessionManager(requireContext());

        adapter = new ProductoAdapter(ProductoAdapter.TYPE_HORIZONTAL);
        adapter.setOnProductClickListener(this::abrirDetalleProducto);
        adapter.setOnProductAddListener(this::abrirDetalleProducto);
        binding.rvOrdenados.setAdapter(adapter);

        cargarFavoritos();
    }

    private void cargarFavoritos() {
        ApiClient.getService().listarFavoritos(session.getUserId()).enqueue(new Callback<ApiResponse<List<Producto>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Producto>>> call, @NonNull Response<ApiResponse<List<Producto>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    List<Producto> favs = response.body().getData();
                    adapter.submitList(favs);
                    int count = favs != null ? favs.size() : 0;
                    binding.tvNuevo.setText(count + " items");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Producto>>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error al cargar favoritos", Toast.LENGTH_SHORT).show();
                }
            }
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
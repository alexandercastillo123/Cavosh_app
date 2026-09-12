package es.oaemdl.cavoshcafe2026.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.data.api.ApiClient;
import es.oaemdl.cavoshcafe2026.data.model.ApiResponse;
import es.oaemdl.cavoshcafe2026.data.model.Pedido;
import es.oaemdl.cavoshcafe2026.data.pref.SessionManager;
import es.oaemdl.cavoshcafe2026.databinding.FragmentMisPedidosBinding;
import es.oaemdl.cavoshcafe2026.ui.adapter.PedidoAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class MisPedidos extends Fragment {

    private FragmentMisPedidosBinding binding;
    private SessionManager session;
    private PedidoAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMisPedidosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        session = new SessionManager(requireContext());

        adapter = new PedidoAdapter(pedido -> {
            OrderDetailBottomSheet sheet = OrderDetailBottomSheet.newInstance(pedido);
            sheet.show(getChildFragmentManager(), "order_detail");
        });
        binding.rvPedidos.setAdapter(adapter);

        binding.btnMisPedidosBack.setOnClickListener(v ->
                Navigation.findNavController(view).popBackStack());

        cargarPedidos();
    }

    private void cargarPedidos() {
        binding.progressOrders.setVisibility(View.VISIBLE);
        binding.layoutEmptyOrders.setVisibility(View.GONE);

        ApiClient.getService().listarPedidosUsuario(session.getUserId())
                .enqueue(new Callback<ApiResponse<List<Pedido>>>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse<List<Pedido>>> call,
                                           @NonNull Response<ApiResponse<List<Pedido>>> response) {
                        if (!isAdded()) return;
                        binding.progressOrders.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null
                                && response.body().isSuccess()) {
                            List<Pedido> pedidos = response.body().getData();
                            if (pedidos == null || pedidos.isEmpty()) {
                                binding.layoutEmptyOrders.setVisibility(View.VISIBLE);
                            } else {
                                adapter.submitList(pedidos);
                            }
                        } else {
                            binding.layoutEmptyOrders.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse<List<Pedido>>> call,
                                          @NonNull Throwable t) {
                        if (!isAdded()) return;
                        binding.progressOrders.setVisibility(View.GONE);
                        binding.layoutEmptyOrders.setVisibility(View.VISIBLE);
                        Toast.makeText(requireContext(), "Error de red: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

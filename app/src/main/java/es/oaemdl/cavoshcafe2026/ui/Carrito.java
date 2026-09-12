package es.oaemdl.cavoshcafe2026.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.data.api.ApiClient;
import es.oaemdl.cavoshcafe2026.data.model.ApiResponse;
import es.oaemdl.cavoshcafe2026.data.model.CarritoItem;
import es.oaemdl.cavoshcafe2026.data.model.CarritoResumen;
import es.oaemdl.cavoshcafe2026.data.model.CuponResponse;
import es.oaemdl.cavoshcafe2026.data.model.CuponValidateRequest;
import es.oaemdl.cavoshcafe2026.data.pref.SessionManager;
import es.oaemdl.cavoshcafe2026.databinding.FragmentCarritoBinding;
import es.oaemdl.cavoshcafe2026.ui.adapter.CarritoAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Locale;

public class Carrito extends Fragment {
    private FragmentCarritoBinding binding;
    private SessionManager session;
    private CarritoAdapter adapter;
    private double currentSubtotal = 0.0;
    private double currentDiscount = 0.0;
    private double currentTotal = 0.0;
    private String appliedCoupon = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCarritoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        session = new SessionManager(requireContext());

        adapter = new CarritoAdapter(new CarritoAdapter.OnCarritoInteractionListener() {
            @Override
            public void onQuantityChange(CarritoItem item, int newQuantity) {
                actualizarCantidad(item.getIdCarrito(), newQuantity);
            }

            @Override
            public void onItemDelete(CarritoItem item) {
                eliminarItem(item.getIdCarrito());
            }
        });
        binding.rvOrdenados.setAdapter(adapter);

        binding.btnApply.setOnClickListener(v -> aplicarCupon());
        binding.btnCheckOut.setOnClickListener(v -> abrirCheckout());

        cargarCarrito();
    }

    private void cargarCarrito() {
        ApiClient.getService().obtenerCarrito(session.getUserId()).enqueue(new Callback<ApiResponse<CarritoResumen>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<CarritoResumen>> call, @NonNull Response<ApiResponse<CarritoResumen>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    CarritoResumen resumen = response.body().getData();
                    if (resumen != null) {
                        adapter.submitList(resumen.getItems());
                        currentSubtotal = resumen.getSubtotal();
                        recalcularTotales();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<CarritoResumen>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error al cargar carrito", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void actualizarCantidad(int idCarrito, int nuevaCantidad) {
        ApiClient.getService().actualizarCantidadCarrito(idCarrito, session.getUserId(), nuevaCantidad).enqueue(new Callback<ApiResponse<CarritoItem>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<CarritoItem>> call, @NonNull Response<ApiResponse<CarritoItem>> response) {
                if (isAdded()) {
                    cargarCarrito();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<CarritoItem>> call, @NonNull Throwable t) {}
        });
    }

    private void eliminarItem(int idCarrito) {
        ApiClient.getService().eliminarDelCarrito(idCarrito, session.getUserId()).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call, @NonNull Response<ApiResponse<Void>> response) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
                    cargarCarrito();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {}
        });
    }

    private void aplicarCupon() {
        String codigo = binding.etPromoCode.getText().toString().trim();
        if (codigo.isEmpty()) {
            Toast.makeText(requireContext(), "Ingresa un código promocional", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getService().validarCupon(new CuponValidateRequest(codigo, currentSubtotal)).enqueue(new Callback<ApiResponse<CuponResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<CuponResponse>> call, @NonNull Response<ApiResponse<CuponResponse>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    CuponResponse cr = response.body().getData();
                    if (cr != null && cr.isValido()) {
                        appliedCoupon = cr.getCodigo();
                        currentDiscount = cr.getDescuento();
                        recalcularTotales();
                        Toast.makeText(requireContext(), cr.getMensaje(), Toast.LENGTH_SHORT).show();
                    } else {
                        String msg = cr != null ? cr.getMensaje() : "Cupón inválido";
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<CuponResponse>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error al validar cupón", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void recalcularTotales() {
        currentTotal = Math.max(0, currentSubtotal - currentDiscount);
        binding.tvCartValue.setText(String.format(Locale.US, "$%.2f", currentSubtotal));
        binding.tvDiscountValue.setText(String.format(Locale.US, "$%.2f", currentDiscount));
        binding.tvTotalValue.setText(String.format(Locale.US, "$%.2f", currentTotal));
    }

    private void abrirCheckout() {
        if (currentSubtotal <= 0) {
            Toast.makeText(requireContext(), "Tu carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        CheckoutBottomSheet sheet = CheckoutBottomSheet.newInstance(currentTotal, appliedCoupon, pedido -> {
            cargarCarrito();
            OrderSuccessBottomSheet successSheet = OrderSuccessBottomSheet.newInstance(pedido, () -> {
                NavController nc = Navigation.findNavController(requireView());
                nc.navigate(R.id.navigation_inicio);
            });
            successSheet.show(getChildFragmentManager(), "order_success");
        });
        sheet.show(getChildFragmentManager(), "checkout");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
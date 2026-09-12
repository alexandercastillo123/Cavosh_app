package es.oaemdl.cavoshcafe2026.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.data.api.ApiClient;
import es.oaemdl.cavoshcafe2026.data.model.ApiResponse;
import es.oaemdl.cavoshcafe2026.data.model.DetallePedido;
import es.oaemdl.cavoshcafe2026.data.model.Pedido;
import es.oaemdl.cavoshcafe2026.databinding.SheetOrderDetailBinding;
import es.oaemdl.cavoshcafe2026.ui.adapter.DetallePedidoAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import java.util.Locale;

public class OrderDetailBottomSheet extends BottomSheetDialogFragment {

    private SheetOrderDetailBinding b;
    private Pedido pedido;
    private DetallePedidoAdapter detalleAdapter;

    public static OrderDetailBottomSheet newInstance(Pedido pedido) {
        OrderDetailBottomSheet sheet = new OrderDetailBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable("pedido", pedido);
        sheet.setArguments(args);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = SheetOrderDetailBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            pedido = (Pedido) getArguments().getSerializable("pedido");
        }

        detalleAdapter = new DetallePedidoAdapter();
        b.rvDetalles.setAdapter(detalleAdapter);

        b.btnOrderDetailBack.setOnClickListener(v -> dismiss());
        b.btnOrderDetailClose.setOnClickListener(v -> dismiss());

        if (pedido != null) {
            // Mostrar datos del pedido que ya tenemos
            mostrarPedido(pedido);
            // Actualizar desde API para tener datos frescos con detalles
            cargarDetallePedido(pedido.getIdPedido());
        }
    }

    private void cargarDetallePedido(int idPedido) {
        ApiClient.getService().obtenerPedido(idPedido).enqueue(new Callback<ApiResponse<Pedido>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Pedido>> call,
                                   @NonNull Response<ApiResponse<Pedido>> response) {
                if (isAdded() && response.isSuccessful()
                        && response.body() != null && response.body().isSuccess()) {
                    Pedido updated = response.body().getData();
                    if (updated != null) {
                        pedido = updated;
                        mostrarPedido(pedido);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Pedido>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error al cargar detalles", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mostrarPedido(Pedido p) {
        // Número y estado
        b.tvDetailNumero.setText(String.format("#%s", p.getNumeroPedido()));
        b.tvDetailEstadoBadge.setText(p.getEstado());

        // Sucursal
        if (p.getSucursal() != null) {
            b.tvDetailSucursal.setText(String.format("%s - %s",
                    p.getSucursal().getNombre(), p.getSucursal().getDireccion()));
        }

        // Fecha y hora
        String fechaHora = !p.getFechaEntrega().isEmpty()
                ? p.getFechaEntrega() + ", " + p.getHoraEntrega()
                : p.getHoraEntrega();
        b.tvDetailFechaHora.setText(fechaHora);

        // Pago
        String pago = p.getMetodoPago();
        if ("CARD".equals(pago) && p.getTarjetaUltimos4() != null) {
            pago = String.format("Card  ···· %s", p.getTarjetaUltimos4());
        }
        b.tvDetailPago.setText(pago);

        // Totales
        b.tvDetailSubtotal.setText(String.format(Locale.US, "$%.2f", p.getSubtotal()));
        b.tvDetailDescuento.setText(String.format(Locale.US, "-$%.2f", p.getDescuento()));
        b.tvDetailTotal.setText(String.format(Locale.US, "$%.2f", p.getTotal()));

        // Detalles de productos
        List<DetallePedido> detalles = p.getDetalles();
        if (detalles != null && !detalles.isEmpty()) {
            detalleAdapter.submitList(detalles);
        }

        // Actualizar tracking según estado
        actualizarTracking(p.getEstado());
    }

    private void actualizarTracking(String estado) {
        int activeColor = ContextCompat.getColor(requireContext(), R.color.carmine_pink);
        int inactiveColor = ContextCompat.getColor(requireContext(), R.color.light_silver);

        // Por defecto: solo "placed" activo
        b.dotPlaced.setBackgroundResource(R.drawable.sh_notificacion_on);
        b.dotPreparing.setBackgroundResource(R.drawable.sh_btn_light_silver);
        b.dotReady.setBackgroundResource(R.drawable.sh_btn_light_silver);
        b.linePlacedPreparing.setBackgroundColor(inactiveColor);
        b.linePreparingReady.setBackgroundColor(inactiveColor);

        if ("PREPARING".equals(estado) || "READY".equals(estado)) {
            b.dotPreparing.setBackgroundResource(R.drawable.sh_notificacion_on);
            b.linePlacedPreparing.setBackgroundColor(activeColor);
        }
        if ("READY".equals(estado)) {
            b.dotReady.setBackgroundResource(R.drawable.sh_notificacion_on);
            b.linePreparingReady.setBackgroundColor(activeColor);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}

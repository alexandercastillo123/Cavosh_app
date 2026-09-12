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
import es.oaemdl.cavoshcafe2026.data.model.Pedido;
import es.oaemdl.cavoshcafe2026.data.model.PedidoCreateRequest;
import es.oaemdl.cavoshcafe2026.data.pref.SessionManager;
import es.oaemdl.cavoshcafe2026.databinding.SheetCheckoutBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.time.LocalDate;
import java.util.Locale;

public class CheckoutBottomSheet extends BottomSheetDialogFragment {

    private SheetCheckoutBinding b;
    private int currentStep = 1; // 1: Delivery, 2: Payment, 3: Review
    private String metodoEntrega = "PICKUP";
    private String metodoPago = "CARD";
    private String tarjetaUltimos4 = "2048";
    private double totalAmount = 11.20;
    private String codigoCupon = null;

    private OnOrderPlacedListener orderPlacedListener;

    public interface OnOrderPlacedListener {
        void onOrderPlaced(Pedido pedido);
    }

    public static CheckoutBottomSheet newInstance(double total, String cupon, OnOrderPlacedListener listener) {
        CheckoutBottomSheet sheet = new CheckoutBottomSheet();
        sheet.totalAmount = total;
        sheet.codigoCupon = cupon;
        sheet.orderPlacedListener = listener;
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = SheetCheckoutBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager session = new SessionManager(requireContext());

        b.btnCheckoutBack.setOnClickListener(v -> {
            if (currentStep > 1) {
                setStep(currentStep - 1);
            } else {
                dismiss();
            }
        });

        // Paso 1: Pick up vs Delivery
        b.btnDeliveryPickup.setOnClickListener(v -> {
            metodoEntrega = "PICKUP";
            b.btnDeliveryPickup.setBackgroundResource(R.drawable.sh_btn_carmine_pink);
            b.btnDeliveryPickup.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            b.btnDeliveryDelivery.setBackground(null);
            b.btnDeliveryDelivery.setTextColor(ContextCompat.getColor(requireContext(), R.color.gunmetal));
        });

        b.btnDeliveryDelivery.setOnClickListener(v -> {
            metodoEntrega = "DELIVERY";
            b.btnDeliveryDelivery.setBackgroundResource(R.drawable.sh_btn_carmine_pink);
            b.btnDeliveryDelivery.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            b.btnDeliveryPickup.setBackground(null);
            b.btnDeliveryPickup.setTextColor(ContextCompat.getColor(requireContext(), R.color.gunmetal));
        });

        // Paso 2: Método de Pago
        b.btnPayCard.setOnClickListener(v -> selectPaymentMethod("CARD", b.btnPayCard));
        b.btnPayCash.setOnClickListener(v -> selectPaymentMethod("CASH", b.btnPayCash));
        b.btnPayPaypal.setOnClickListener(v -> selectPaymentMethod("PAYPAL", b.btnPayPaypal));

        b.rgCards.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbCardVisa) {
                tarjetaUltimos4 = "1234";
            } else {
                tarjetaUltimos4 = "2048";
            }
        });

        // Paso 3: Review values
        b.tvReviewCafe.setText(String.format("%s - %s", session.getSelectedCafeName(), session.getSelectedCafeAddress()));
        b.tvReviewTotal.setText(String.format(Locale.US, "$%.2f", totalAmount));

        // Botón Next / Place Order
        b.btnCheckoutNext.setOnClickListener(v -> {
            if (currentStep == 1) {
                setStep(2);
            } else if (currentStep == 2) {
                setStep(3);
            } else {
                realizarPedido(session);
            }
        });

        setStep(1);
    }

    private void selectPaymentMethod(String method, com.google.android.material.button.MaterialButton btn) {
        metodoPago = method;
        int active = ContextCompat.getColor(requireContext(), R.color.carmine_pink);
        int inactive = ContextCompat.getColor(requireContext(), R.color.bright_gray);
        int activeText = ContextCompat.getColor(requireContext(), R.color.white);
        int inactiveText = ContextCompat.getColor(requireContext(), R.color.gunmetal);

        b.btnPayCard.setBackgroundColor(inactive);
        b.btnPayCard.setTextColor(inactiveText);
        b.btnPayCash.setBackgroundColor(inactive);
        b.btnPayCash.setTextColor(inactiveText);
        b.btnPayPaypal.setBackgroundColor(inactive);
        b.btnPayPaypal.setTextColor(inactiveText);

        btn.setBackgroundColor(active);
        btn.setTextColor(activeText);

        b.rgCards.setVisibility("CARD".equals(method) ? View.VISIBLE : View.GONE);
    }

    private void setStep(int step) {
        currentStep = step;
        b.layoutStep1Delivery.setVisibility(step == 1 ? View.VISIBLE : View.GONE);
        b.layoutStep2Payment.setVisibility(step == 2 ? View.VISIBLE : View.GONE);
        b.layoutStep3Review.setVisibility(step == 3 ? View.VISIBLE : View.GONE);

        if (step == 1) {
            b.tvCheckoutStepTitle.setText("Delivery Method");
            b.btnCheckoutNext.setText("Next");
        } else if (step == 2) {
            b.tvCheckoutStepTitle.setText("Payment Method");
            b.btnCheckoutNext.setText("Next");
        } else {
            b.tvCheckoutStepTitle.setText("Review Order");
            b.btnCheckoutNext.setText("Place Order");
            b.tvReviewPayment.setText(String.format("Payment: %s (•••• %s)", metodoPago, tarjetaUltimos4));
        }
    }

    private void realizarPedido(SessionManager session) {
        b.btnCheckoutNext.setEnabled(false);

        PedidoCreateRequest req = new PedidoCreateRequest(
                session.getUserId(),
                session.getSelectedCafeId(),
                metodoEntrega,
                null,
                "08:30 AM",
                metodoPago,
                tarjetaUltimos4,
                codigoCupon
        );

        ApiClient.getService().crearPedido(req).enqueue(new Callback<ApiResponse<Pedido>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Pedido>> call, @NonNull Response<ApiResponse<Pedido>> response) {
                if (isAdded()) {
                    b.btnCheckoutNext.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Pedido pedido = response.body().getData();
                        dismiss();
                        if (orderPlacedListener != null) {
                            orderPlacedListener.onOrderPlaced(pedido);
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error al procesar pedido", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Pedido>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    b.btnCheckoutNext.setEnabled(true);
                    Toast.makeText(requireContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}

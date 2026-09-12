package es.oaemdl.cavoshcafe2026.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import es.oaemdl.cavoshcafe2026.data.model.Pedido;
import es.oaemdl.cavoshcafe2026.databinding.SheetOrderSuccessBinding;

public class OrderSuccessBottomSheet extends BottomSheetDialogFragment {

    private SheetOrderSuccessBinding b;
    private Pedido pedido;
    private Runnable onHomeClicked;

    public static OrderSuccessBottomSheet newInstance(Pedido pedido, Runnable onHomeClicked) {
        OrderSuccessBottomSheet sheet = new OrderSuccessBottomSheet();
        sheet.pedido = pedido;
        sheet.onHomeClicked = onHomeClicked;
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = SheetOrderSuccessBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (pedido != null) {
            b.tvOrderNumberBadge.setText(pedido.getNumeroPedido());
            b.tvTrackingTime.setText(pedido.getHoraEntrega());
            if (pedido.getSucursal() != null) {
                b.tvTrackingCafe.setText(String.format("%s - %s", pedido.getSucursal().getNombre(), pedido.getSucursal().getDireccion()));
            }
        }

        b.btnBackHome.setOnClickListener(v -> {
            dismiss();
            if (onHomeClicked != null) {
                onHomeClicked.run();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}

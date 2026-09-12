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
import com.squareup.picasso.Picasso;
import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.data.api.ApiClient;
import es.oaemdl.cavoshcafe2026.data.model.ApiResponse;
import es.oaemdl.cavoshcafe2026.data.model.CarritoItem;
import es.oaemdl.cavoshcafe2026.data.model.CarritoItemRequest;
import es.oaemdl.cavoshcafe2026.data.model.Producto;
import es.oaemdl.cavoshcafe2026.data.pref.SessionManager;
import es.oaemdl.cavoshcafe2026.databinding.SheetProductDetailBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Locale;

public class ProductDetailBottomSheet extends BottomSheetDialogFragment {

    private SheetProductDetailBinding b;
    private Producto producto;
    private int quantity = 1;
    private String selectedSize = "Small";
    private double basePrice = 4.0;
    private double sizeExtra = 0.0;
    private double milkExtra = 0.0;
    private double creamExtra = 0.0;
    private String selectedMilk = "Full-fat milk";
    private String selectedCream = "Without whipped cream";
    private String selectedCaffeine = "With caffeine";

    private OnProductAddedToCartListener cartListener;

    public interface OnProductAddedToCartListener {
        void onProductAdded();
    }

    public static ProductDetailBottomSheet newInstance(Producto p) {
        ProductDetailBottomSheet sheet = new ProductDetailBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable("producto", p);
        sheet.setArguments(args);
        return sheet;
    }

    public void setOnProductAddedListener(OnProductAddedToCartListener listener) {
        this.cartListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = SheetProductDetailBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            producto = (Producto) getArguments().getSerializable("producto");
        }

        if (producto == null) {
            dismiss();
            return;
        }

        basePrice = producto.getPrecio();
        b.tvDetailTitle.setText(producto.getNombre());
        b.tvDetailDescription.setText(producto.getDescripcion());

        if (producto.getImagenUrl() != null && !producto.getImagenUrl().isBlank()) {
            Picasso.get().load(producto.getImagenUrl()).placeholder(R.drawable.ic_img_cafe).into(b.ivProductDetail);
        } else {
            b.ivProductDetail.setImageResource(R.drawable.ic_img_cafe);
        }

        b.btnBack.setOnClickListener(v -> dismiss());

        // Stepper
        b.btnDetailPlus.setOnClickListener(v -> {
            quantity++;
            b.tvDetailQuantity.setText(String.valueOf(quantity));
            updatePrice();
        });

        b.btnDetailMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                b.tvDetailQuantity.setText(String.valueOf(quantity));
                updatePrice();
            }
        });

        // Tamaño
        b.btnSizeSmall.setOnClickListener(v -> selectSize("Small", 0.0, b.btnSizeSmall));
        b.btnSizeMedium.setOnClickListener(v -> selectSize("Medium", 0.50, b.btnSizeMedium));
        b.btnSizeLarge.setOnClickListener(v -> selectSize("Large", 1.00, b.btnSizeLarge));

        // Leche
        b.rgMilk.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbMilkOat) {
                selectedMilk = "Oat milk";
                milkExtra = 0.70;
            } else if (checkedId == R.id.rbMilkAlmond) {
                selectedMilk = "Almond milk";
                milkExtra = 0.70;
            } else {
                selectedMilk = "Full-fat milk";
                milkExtra = 0.0;
            }
            updatePrice();
        });

        // Crema batida
        b.cbWhippedCream.setOnCheckedChangeListener((btn, isChecked) -> {
            if (isChecked) {
                selectedCream = "With whipped cream";
                creamExtra = 0.50;
            } else {
                selectedCream = "Without whipped cream";
                creamExtra = 0.0;
            }
            updatePrice();
        });

        // Cafeína
        b.cbNoCaffeine.setOnCheckedChangeListener((btn, isChecked) -> {
            selectedCaffeine = isChecked ? "Without caffeine" : "With caffeine";
        });

        // Botón Add to Cart
        b.btnAddToCart.setOnClickListener(v -> agregarAlCarrito());

        updatePrice();
    }

    private void selectSize(String size, double extra, com.google.android.material.button.MaterialButton selectedBtn) {
        selectedSize = size;
        sizeExtra = extra;

        int activeColor = ContextCompat.getColor(requireContext(), R.color.carmine_pink);
        int inactiveColor = ContextCompat.getColor(requireContext(), R.color.bright_gray);
        int activeText = ContextCompat.getColor(requireContext(), R.color.white);
        int inactiveText = ContextCompat.getColor(requireContext(), R.color.gunmetal);

        b.btnSizeSmall.setBackgroundColor(inactiveColor);
        b.btnSizeSmall.setTextColor(inactiveText);
        b.btnSizeMedium.setBackgroundColor(inactiveColor);
        b.btnSizeMedium.setTextColor(inactiveText);
        b.btnSizeLarge.setBackgroundColor(inactiveColor);
        b.btnSizeLarge.setTextColor(inactiveText);

        selectedBtn.setBackgroundColor(activeColor);
        selectedBtn.setTextColor(activeText);

        updatePrice();
    }

    private void updatePrice() {
        double unitPrice = basePrice + sizeExtra + milkExtra + creamExtra;
        double totalPrice = unitPrice * quantity;
        b.tvDetailTotalPrice.setText(String.format(Locale.US, "$%.2f", totalPrice));
    }

    private void agregarAlCarrito() {
        SessionManager session = new SessionManager(requireContext());
        double unitPrice = basePrice + sizeExtra + milkExtra + creamExtra;

        CarritoItemRequest req = new CarritoItemRequest(
                session.getUserId(),
                producto.getIdProducto(),
                quantity,
                selectedSize,
                selectedMilk,
                selectedCream,
                selectedCaffeine,
                unitPrice
        );

        b.btnAddToCart.setEnabled(false);
        ApiClient.getService().agregarAlCarrito(req).enqueue(new Callback<ApiResponse<CarritoItem>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<CarritoItem>> call, @NonNull Response<ApiResponse<CarritoItem>> response) {
                if (isAdded()) {
                    b.btnAddToCart.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toast.makeText(requireContext(), "Agregado al carrito: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
                        if (cartListener != null) cartListener.onProductAdded();
                        dismiss();
                    } else {
                        Toast.makeText(requireContext(), "Error al agregar al carrito", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<CarritoItem>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    b.btnAddToCart.setEnabled(true);
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

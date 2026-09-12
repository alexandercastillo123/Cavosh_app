package es.oaemdl.cavoshcafe2026.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.data.model.CarritoItem;
import es.oaemdl.cavoshcafe2026.databinding.ItemAgregarBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.ViewHolder> {

    private final List<CarritoItem> items = new ArrayList<>();
    private final OnCarritoInteractionListener listener;

    public interface OnCarritoInteractionListener {
        void onQuantityChange(CarritoItem item, int newQuantity);
        void onItemDelete(CarritoItem item);
    }

    public CarritoAdapter(OnCarritoInteractionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<CarritoItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAgregarBinding binding = ItemAgregarBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarritoItem item = items.get(position);

        String nombre = item.getProducto() != null ? item.getProducto().getNombre() : "Café";
        holder.b.tvNombreProducto.setText(nombre);

        // Subtítulo con personalizaciones (ej: Small, Oat milk)
        String detalles = String.format("%s, %s", item.getTamano(), item.getTipoLeche());
        holder.b.tvDescripcion.setText(detalles);

        holder.b.tvPrecio.setText(String.format(Locale.US, "$%.2f", item.getSubtotal()));
        holder.b.tvQuantity.setText(String.valueOf(item.getCantidad()));

        if (item.getProducto() != null && item.getProducto().getImagenUrl() != null && !item.getProducto().getImagenUrl().isBlank()) {
            Picasso.get().load(item.getProducto().getImagenUrl()).placeholder(R.drawable.ic_img_cafe).into(holder.b.ivProducto);
        } else {
            holder.b.ivProducto.setImageResource(R.drawable.ic_img_cafe);
        }

        holder.b.btnIncrement.setOnClickListener(v -> {
            if (listener != null) {
                listener.onQuantityChange(item, item.getCantidad() + 1);
            }
        });

        holder.b.btnDecrement.setOnClickListener(v -> {
            if (listener != null) {
                if (item.getCantidad() > 1) {
                    listener.onQuantityChange(item, item.getCantidad() - 1);
                } else {
                    listener.onItemDelete(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemAgregarBinding b;
        ViewHolder(ItemAgregarBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}

package es.oaemdl.cavoshcafe2026.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.data.model.Producto;
import es.oaemdl.cavoshcafe2026.databinding.ItemProductoBinding;
import es.oaemdl.cavoshcafe2026.databinding.ItemProductoHorizontalBinding;
import es.oaemdl.cavoshcafe2026.databinding.ItemProductoVerticalBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_VERTICAL = 1;   // Carrusel "Nuevos" en Inicio
    public static final int TYPE_HORIZONTAL = 2; // Lista "Frecuentes" en Inicio y Favoritos
    public static final int TYPE_GRID = 3;       // Cuadrícula 2 columnas en Menú

    private final int viewType;
    private final List<Producto> items = new ArrayList<>();
    private OnProductClickListener clickListener;
    private OnProductAddListener addListener;
    private OnProductFavoriteListener favoriteListener;

    public interface OnProductClickListener {
        void onProductClick(Producto producto);
    }

    public interface OnProductAddListener {
        void onProductAdd(Producto producto);
    }

    public interface OnProductFavoriteListener {
        void onFavoriteToggle(Producto producto, int position);
    }

    public ProductoAdapter(int viewType) {
        this.viewType = viewType;
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnProductAddListener(OnProductAddListener listener) {
        this.addListener = listener;
    }

    public void setOnProductFavoriteListener(OnProductFavoriteListener listener) {
        this.favoriteListener = listener;
    }

    public void submitList(List<Producto> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_VERTICAL) {
            ItemProductoVerticalBinding binding = ItemProductoVerticalBinding.inflate(inflater, parent, false);
            return new VerticalViewHolder(binding);
        } else if (viewType == TYPE_HORIZONTAL) {
            ItemProductoHorizontalBinding binding = ItemProductoHorizontalBinding.inflate(inflater, parent, false);
            return new HorizontalViewHolder(binding);
        } else {
            ItemProductoBinding binding = ItemProductoBinding.inflate(inflater, parent, false);
            return new GridViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Producto p = items.get(position);
        if (holder instanceof VerticalViewHolder) {
            ((VerticalViewHolder) holder).bind(p);
        } else if (holder instanceof HorizontalViewHolder) {
            ((HorizontalViewHolder) holder).bind(p);
        } else if (holder instanceof GridViewHolder) {
            ((GridViewHolder) holder).bind(p);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VerticalViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductoVerticalBinding b;
        VerticalViewHolder(ItemProductoVerticalBinding b) {
            super(b.getRoot());
            this.b = b;
        }
        void bind(Producto p) {
            b.tvNombreProducto.setText(p.getNombre());
            b.tvPrecio.setText(String.format(Locale.US, "$%.2f", p.getPrecio()));
            cargarImagen(p, b.ivProducto);
            b.getRoot().setOnClickListener(v -> {
                if (clickListener != null) clickListener.onProductClick(p);
            });
            b.btnAgregar.setOnClickListener(v -> {
                if (addListener != null) addListener.onProductAdd(p);
            });
        }
    }

    class HorizontalViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductoHorizontalBinding b;
        HorizontalViewHolder(ItemProductoHorizontalBinding b) {
            super(b.getRoot());
            this.b = b;
        }
        void bind(Producto p) {
            b.tvNombreProducto.setText(p.getNombre());
            b.tvDescripcion.setText(p.getDescripcion());
            b.tvPrecio.setText(String.format(Locale.US, "$%.2f", p.getPrecio()));
            cargarImagen(p, b.ivProducto);
            b.getRoot().setOnClickListener(v -> {
                if (clickListener != null) clickListener.onProductClick(p);
            });
            b.btnAgregar.setOnClickListener(v -> {
                if (addListener != null) addListener.onProductAdd(p);
            });
        }
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductoBinding b;
        GridViewHolder(ItemProductoBinding b) {
            super(b.getRoot());
            this.b = b;
        }
        void bind(Producto p) {
            b.tvNombreProducto.setText(p.getNombre());
            b.tvPrecio.setText(String.format(Locale.US, "$%.2f", p.getPrecio()));
            cargarImagen(p, b.ivProducto);

            actualizarIconoFavorito(p);

            b.ivFavorito.setOnClickListener(v -> {
                if (favoriteListener != null) {
                    favoriteListener.onFavoriteToggle(p, getAdapterPosition());
                }
            });

            b.getRoot().setOnClickListener(v -> {
                if (clickListener != null) clickListener.onProductClick(p);
            });
            b.btnAgregar.setOnClickListener(v -> {
                if (addListener != null) addListener.onProductAdd(p);
            });
        }

        private void actualizarIconoFavorito(Producto p) {
            if (p.isFavorito()) {
                b.ivFavorito.setImageResource(R.drawable.ic_favoritos_selected);
            } else {
                b.ivFavorito.setImageResource(R.drawable.ic_favoritos);
            }
        }
    }

    private void cargarImagen(Producto p, android.widget.ImageView iv) {
        if (p.getImagenUrl() != null && !p.getImagenUrl().isBlank()) {
            Picasso.get().load(p.getImagenUrl()).placeholder(R.drawable.ic_img_cafe).error(R.drawable.ic_img_cafe).into(iv);
        } else {
            iv.setImageResource(R.drawable.ic_img_cafe);
        }
    }
}

package es.oaemdl.cavoshcafe2026.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.model.Producto;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private final Context context;
    private final List<Producto> productos;

    public ProductoAdapter(Context context, List<Producto> productos) {
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto_vertical, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.tvNombreProducto.setText(producto.getDetalle());
        holder.tvPrecio.setText(String.format("S/ %s", new DecimalFormat("0.00").format(producto.getPrecio())));

        Picasso.get()
                .load("https://cavosh.com/images/" + producto.getId() + ".jpg")
                .fit().centerCrop()
                .placeholder(R.drawable.ic_img_cafe)
                .error(R.drawable.ic_img_cafe)
                .into(holder.ivProducto);

        holder.itemView.setOnClickListener(v -> {
            // Click en item
        });
    }

    @Override
    public int getItemCount() {
        return productos != null ? productos.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProducto;
        ImageButton btnAgregar;
        TextView tvNombreProducto;
        TextView tvPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducto = itemView.findViewById(R.id.ivProducto);
            btnAgregar = itemView.findViewById(R.id.btnAgregar);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}
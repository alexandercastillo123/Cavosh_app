package es.oaemdl.cavoshcafe2026.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.model.Producto;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    Context context;
    List<Producto> productos = null;
    public ProductoAdapter(Context context, List<Producto> productos) {
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate( R.layout.item_producto_vertical, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.ViewHolder holder, int position) {
        Producto producto;


    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProducto;
        ImageView ivFavorito;
        ImageView ivAgregar;
        TextView tvProducto;
        TextView tvPrecio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProducto = itemView.findViewById(R.id.ivProducto_itemVertical);
            ivFavorito = itemView.findViewById(R.id.ivFavorito);
            ivAgregar = itemView.findViewById(R.id.btnAgregar);
            tvProducto = itemView.findViewById(R.id.tvProducto_itemVertical);
            tvPrecio = itemView.findViewById(R.id.tvPrecio_itemVertical);
        }
    }
}
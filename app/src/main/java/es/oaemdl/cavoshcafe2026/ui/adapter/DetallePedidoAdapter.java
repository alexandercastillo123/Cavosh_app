package es.oaemdl.cavoshcafe2026.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.data.model.DetallePedido;
import java.util.Locale;

public class DetallePedidoAdapter extends ListAdapter<DetallePedido, DetallePedidoAdapter.ViewHolder> {

    public DetallePedidoAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<DetallePedido> DIFF_CALLBACK = new DiffUtil.ItemCallback<DetallePedido>() {
        @Override
        public boolean areItemsTheSame(@NonNull DetallePedido o, @NonNull DetallePedido n) {
            return o.getIdDetalle() == n.getIdDetalle();
        }
        @Override
        public boolean areContentsTheSame(@NonNull DetallePedido o, @NonNull DetallePedido n) {
            return o.getSubtotal() == n.getSubtotal();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detalle_pedido, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCantidad;
        private final TextView tvNombreProducto;
        private final TextView tvPersonalizacion;
        private final TextView tvSubtotal;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvPersonalizacion = itemView.findViewById(R.id.tvPersonalizacion);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
        }

        void bind(DetallePedido detalle) {
            tvCantidad.setText(String.valueOf(detalle.getCantidad()));

            String nombre = detalle.getProducto() != null
                    ? detalle.getProducto().getNombre() : "—";
            tvNombreProducto.setText(nombre);

            // Personalización: tamaño + leche
            StringBuilder sb = new StringBuilder();
            if (detalle.getTamano() != null && !detalle.getTamano().isEmpty()) {
                sb.append(detalle.getTamano());
            }
            if (detalle.getTipoLeche() != null && !detalle.getTipoLeche().isEmpty()) {
                if (sb.length() > 0) sb.append(" · ");
                sb.append(detalle.getTipoLeche());
            }
            if (detalle.getConCrema() != null && detalle.getConCrema().contains("With whipped")) {
                sb.append(" · Cream");
            }
            tvPersonalizacion.setText(sb.toString());

            tvSubtotal.setText(String.format(Locale.US, "$%.2f", detalle.getSubtotal()));
        }
    }
}

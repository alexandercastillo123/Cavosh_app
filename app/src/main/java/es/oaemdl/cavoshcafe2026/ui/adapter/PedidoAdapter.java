package es.oaemdl.cavoshcafe2026.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.data.model.Pedido;
import java.util.Locale;

public class PedidoAdapter extends ListAdapter<Pedido, PedidoAdapter.ViewHolder> {

    public interface OnPedidoClickListener {
        void onClick(Pedido pedido);
    }

    private final OnPedidoClickListener listener;

    public PedidoAdapter(OnPedidoClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Pedido> DIFF_CALLBACK = new DiffUtil.ItemCallback<Pedido>() {
        @Override
        public boolean areItemsTheSame(@NonNull Pedido oldItem, @NonNull Pedido newItem) {
            return oldItem.getIdPedido() == newItem.getIdPedido();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Pedido oldItem, @NonNull Pedido newItem) {
            return oldItem.getEstado().equals(newItem.getEstado())
                    && oldItem.getTotal() == newItem.getTotal();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNumero;
        private final TextView tvEstadoBadge;
        private final TextView tvSucursal;
        private final TextView tvFecha;
        private final TextView tvTotal;
        private final CardView cardPedido;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumero = itemView.findViewById(R.id.tvNumero);
            tvEstadoBadge = itemView.findViewById(R.id.tvEstadoBadge);
            tvSucursal = itemView.findViewById(R.id.tvSucursal);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            cardPedido = itemView.findViewById(R.id.cardPedido);
        }

        void bind(Pedido pedido) {
            tvNumero.setText(String.format("Order #%s", pedido.getNumeroPedido()));
            tvEstadoBadge.setText(pedido.getEstado());
            tvTotal.setText(String.format(Locale.US, "$%.2f", pedido.getTotal()));

            String sucursalInfo = pedido.getSucursal() != null
                    ? pedido.getSucursal().getNombre() + " - " + pedido.getSucursal().getDireccion()
                    : "";
            tvSucursal.setText(sucursalInfo);

            String fechaHora = "";
            if (!pedido.getFechaEntrega().isEmpty() && !pedido.getHoraEntrega().isEmpty()) {
                fechaHora = pedido.getFechaEntrega() + ", " + pedido.getHoraEntrega();
            } else if (!pedido.getHoraEntrega().isEmpty()) {
                fechaHora = pedido.getHoraEntrega();
            }
            tvFecha.setText(fechaHora);

            cardPedido.setOnClickListener(v -> {
                if (listener != null) listener.onClick(pedido);
            });
        }
    }
}

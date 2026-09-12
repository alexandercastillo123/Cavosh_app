package es.oaemdl.cavoshcafe2026.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.oaemdl.cavoshcafe2026.data.model.Sucursal;
import es.oaemdl.cavoshcafe2026.databinding.ItemSucursalBinding;
import java.util.ArrayList;
import java.util.List;

public class SucursalAdapter extends RecyclerView.Adapter<SucursalAdapter.ViewHolder> {

    private final List<Sucursal> items = new ArrayList<>();
    private int selectedId = -1;
    private final OnSucursalSelectedListener listener;

    public interface OnSucursalSelectedListener {
        void onSucursalSelected(Sucursal sucursal);
    }

    public SucursalAdapter(int initialSelectedId, OnSucursalSelectedListener listener) {
        this.selectedId = initialSelectedId;
        this.listener = listener;
    }

    public void submitList(List<Sucursal> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public void setSelectedId(int id) {
        this.selectedId = id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSucursalBinding binding = ItemSucursalBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sucursal s = items.get(position);
        holder.b.tvNombreCafe.setText(s.getNombre());
        holder.b.tvDireccion.setText(s.getDireccion());
        holder.b.tvHorario.setText(s.getHorarioAtencion());

        boolean isSelected = (s.getIdSucursal() == selectedId);
        holder.b.rbSelected.setChecked(isSelected);

        holder.itemView.setOnClickListener(v -> {
            selectedId = s.getIdSucursal();
            notifyDataSetChanged();
            if (listener != null) {
                listener.onSucursalSelected(s);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemSucursalBinding b;
        ViewHolder(ItemSucursalBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}

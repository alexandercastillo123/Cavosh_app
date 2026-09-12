package es.oaemdl.cavoshcafe2026.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.databinding.ItemCategoriaBinding;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private final List<String> categorias;
    private int selectedPosition = 0;
    private final OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(String categoria);
    }

    public CategoriaAdapter(List<String> categorias, OnCategoryClickListener listener) {
        this.categorias = categorias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoriaBinding binding = ItemCategoriaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String cat = categorias.get(position);
        holder.binding.tvCategoryName.setText(cat);

        boolean isSelected = (position == selectedPosition);
        if (isSelected) {
            holder.binding.containerCategory.setBackgroundResource(R.drawable.sh_btn_carmine_pink);
            holder.binding.tvCategoryName.setTextColor(Color.WHITE);
        } else {
            holder.binding.containerCategory.setBackgroundResource(R.drawable.bg_chip);
            holder.binding.tvCategoryName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gunmetal));
        }

        holder.itemView.setOnClickListener(v -> {
            int prev = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(prev);
            notifyItemChanged(selectedPosition);
            if (listener != null) {
                listener.onCategoryClick(cat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemCategoriaBinding binding;
        ViewHolder(ItemCategoriaBinding b) {
            super(b.getRoot());
            this.binding = b;
        }
    }
}

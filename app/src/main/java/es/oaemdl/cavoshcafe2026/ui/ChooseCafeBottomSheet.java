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
import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.data.api.ApiClient;
import es.oaemdl.cavoshcafe2026.data.model.ApiResponse;
import es.oaemdl.cavoshcafe2026.data.model.Sucursal;
import es.oaemdl.cavoshcafe2026.data.pref.SessionManager;
import es.oaemdl.cavoshcafe2026.databinding.SheetChooseCafeBinding;
import es.oaemdl.cavoshcafe2026.ui.adapter.SucursalAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class ChooseCafeBottomSheet extends BottomSheetDialogFragment {

    private SheetChooseCafeBinding b;
    private SucursalAdapter adapter;
    private Sucursal selectedCafe;
    private OnCafeSavedListener savedListener;

    public interface OnCafeSavedListener {
        void onCafeSaved(Sucursal cafe);
    }

    public void setOnCafeSavedListener(OnCafeSavedListener listener) {
        this.savedListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = SheetChooseCafeBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager session = new SessionManager(requireContext());
        int currentCafeId = session.getSelectedCafeId();
        b.tvChosenCafeName.setText(session.getSelectedCafeAddress());

        adapter = new SucursalAdapter(currentCafeId, sucursal -> {
            selectedCafe = sucursal;
            b.tvChosenCafeName.setText(sucursal.getDireccion());
        });
        b.rvCafes.setAdapter(adapter);

        b.btnCafeBack.setOnClickListener(v -> dismiss());
        b.btnCancelCafe.setOnClickListener(v -> dismiss());

        // Tabs List vs Map
        b.tabList.setOnClickListener(v -> {
            b.tabList.setTextColor(ContextCompat.getColor(requireContext(), R.color.carmine_pink));
            b.tabMap.setTextColor(ContextCompat.getColor(requireContext(), R.color.gunmetal));
            b.rvCafes.setVisibility(View.VISIBLE);
            b.flMapView.setVisibility(View.GONE);
        });

        b.tabMap.setOnClickListener(v -> {
            b.tabMap.setTextColor(ContextCompat.getColor(requireContext(), R.color.carmine_pink));
            b.tabList.setTextColor(ContextCompat.getColor(requireContext(), R.color.gunmetal));
            b.rvCafes.setVisibility(View.GONE);
            b.flMapView.setVisibility(View.VISIBLE);
        });

        b.btnSaveCafe.setOnClickListener(v -> {
            if (selectedCafe != null) {
                session.saveSelectedCafe(selectedCafe);
                Toast.makeText(requireContext(), "Sede guardada: " + selectedCafe.getDireccion(), Toast.LENGTH_SHORT).show();
                if (savedListener != null) {
                    savedListener.onCafeSaved(selectedCafe);
                }
            }
            dismiss();
        });

        cargarSucursales();
    }

    private void cargarSucursales() {
        ApiClient.getService().listarSucursales().enqueue(new Callback<ApiResponse<List<Sucursal>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Sucursal>>> call, @NonNull Response<ApiResponse<List<Sucursal>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Sucursal> list = response.body().getData();
                    adapter.submitList(list);
                    if (selectedCafe == null && !list.isEmpty()) {
                        selectedCafe = list.get(0);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Sucursal>>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error al cargar sedes", Toast.LENGTH_SHORT).show();
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

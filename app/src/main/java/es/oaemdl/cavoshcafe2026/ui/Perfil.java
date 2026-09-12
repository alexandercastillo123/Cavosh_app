package es.oaemdl.cavoshcafe2026.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.data.api.ApiClient;
import es.oaemdl.cavoshcafe2026.data.model.ApiResponse;
import es.oaemdl.cavoshcafe2026.data.model.Usuario;
import es.oaemdl.cavoshcafe2026.data.pref.SessionManager;
import es.oaemdl.cavoshcafe2026.databinding.FragmentPerfilBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Perfil extends Fragment {
    private FragmentPerfilBinding binding;
    private SessionManager session;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        session = new SessionManager(requireContext());

        binding.tvProfileName.setText(session.getUserName());
        binding.tvProfileEmail.setText(session.getUserEmail());
        binding.tvProfilePoints.setText(String.format("⭐ %d points", session.getUserPoints()));

        // Opciones de cuenta
        binding.btnAccountDetails.setOnClickListener(v -> Toast.makeText(requireContext(), "Email: " + session.getUserEmail(), Toast.LENGTH_SHORT).show());
        binding.btnPaymentDetails.setOnClickListener(v -> Toast.makeText(requireContext(), "Método principal: MasterCard •••• 2048", Toast.LENGTH_SHORT).show());
        binding.btnOrderHistory.setOnClickListener(v ->
                navController.navigate(R.id.action_navigation_perfil_to_navigation_mis_pedidos));
        binding.btnRewards.setOnClickListener(v -> Toast.makeText(requireContext(), "Tienes " + session.getUserPoints() + " puntos acumulados", Toast.LENGTH_SHORT).show());

        // Switches de Notificaciones y Ubicación
        binding.switchNotifications.setOnCheckedChangeListener((btn, isChecked) -> {
            actualizarPreferencias(isChecked, binding.switchLocation.isChecked());
        });

        binding.switchLocation.setOnCheckedChangeListener((btn, isChecked) -> {
            actualizarPreferencias(binding.switchNotifications.isChecked(), isChecked);
        });

        // Cerrar sesión
        binding.btnLogout.setOnClickListener(v -> {
            session.logout();
            Toast.makeText(requireContext(), "Sesión finalizada", Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.action_navigation_perfil_to_navigation_login);
        });

        cargarPerfilRemoto();
    }

    private void cargarPerfilRemoto() {
        ApiClient.getService().obtenerPerfil(session.getUserId()).enqueue(new Callback<ApiResponse<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Usuario>> call, @NonNull Response<ApiResponse<Usuario>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    Usuario u = response.body().getData();
                    session.saveUser(u);
                    binding.tvProfileName.setText(u.getNombreCompleto());
                    binding.tvProfileEmail.setText(u.getEmail());
                    binding.tvProfilePoints.setText(String.format("⭐ %d points", u.getPuntos()));
                    binding.switchNotifications.setChecked(u.isRecibirNotificaciones());
                    binding.switchLocation.setChecked(u.isCompartirUbicacion());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Usuario>> call, @NonNull Throwable t) {}
        });
    }

    private void actualizarPreferencias(boolean notif, boolean ubicacion) {
        ApiClient.getService().actualizarPreferencias(session.getUserId(), notif, ubicacion).enqueue(new Callback<ApiResponse<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Usuario>> call, @NonNull Response<ApiResponse<Usuario>> response) {}
            @Override
            public void onFailure(@NonNull Call<ApiResponse<Usuario>> call, @NonNull Throwable t) {}
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
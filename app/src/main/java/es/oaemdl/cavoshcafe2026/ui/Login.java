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
import es.oaemdl.cavoshcafe2026.data.model.LoginRequest;
import es.oaemdl.cavoshcafe2026.data.model.Usuario;
import es.oaemdl.cavoshcafe2026.data.pref.SessionManager;
import es.oaemdl.cavoshcafe2026.databinding.FragmentLoginBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Fragment {
    private FragmentLoginBinding binding;
    private NavController navController;
    private SessionManager session;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        session = new SessionManager(requireContext());

        // Prellenar credenciales de prueba
        if (binding.tilCorreo.getEditText() != null && binding.tilPassword.getEditText() != null) {
            binding.tilCorreo.getEditText().setText("usuario@cavosh.com");
            binding.tilPassword.getEditText().setText("123456");
        }

        // Navegación hacia Registrar
        binding.tvRegistrar.setOnClickListener(v -> navController.navigate(R.id.action_navigation_login_to_navigation_registrar));
        binding.tvRegistrarAhora.setOnClickListener(v -> navController.navigate(R.id.action_navigation_login_to_navigation_registrar));

        binding.btnIniciarSesion.setOnClickListener(v -> iniciarSesion());
    }

    private void iniciarSesion() {
        String email = "";
        String pass = "";

        if (binding.tilCorreo.getEditText() != null) {
            email = binding.tilCorreo.getEditText().getText().toString().trim();
        }
        if (binding.tilPassword.getEditText() != null) {
            pass = binding.tilPassword.getEditText().getText().toString().trim();
        }

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor ingresa correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnIniciarSesion.setEnabled(false);
        ApiClient.getService().login(new LoginRequest(email, pass)).enqueue(new Callback<ApiResponse<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Usuario>> call, @NonNull Response<ApiResponse<Usuario>> response) {
                if (isAdded()) {
                    binding.btnIniciarSesion.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Usuario u = response.body().getData();
                        session.saveUser(u);
                        Toast.makeText(requireContext(), "¡Bienvenido " + u.getNombreCompleto() + "!", Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_navigation_login_to_navigation_inicio);
                    } else {
                        String msg = response.body() != null ? response.body().getMessage() : "Credenciales inválidas";
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Usuario>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    binding.btnIniciarSesion.setEnabled(true);
                    Toast.makeText(requireContext(), "Error de conexión con el backend: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
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
import es.oaemdl.cavoshcafe2026.data.model.RegisterRequest;
import es.oaemdl.cavoshcafe2026.data.model.Usuario;
import es.oaemdl.cavoshcafe2026.data.pref.SessionManager;
import es.oaemdl.cavoshcafe2026.databinding.FragmentRegistrarBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registrar extends Fragment {
    private FragmentRegistrarBinding binding;
    private NavController navController;
    private SessionManager session;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistrarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        session = new SessionManager(requireContext());

        // Navegación hacia Iniciar Sesión
        binding.tvIniciarSesion.setOnClickListener(v -> navController.navigate(R.id.action_navigation_registrar_to_navigation_login));
        binding.tvRegistrarAhora.setOnClickListener(v -> navController.navigate(R.id.action_navigation_registrar_to_navigation_login));

        binding.btnRegistrarse.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nombre = binding.tilNombres.getEditText() != null ? binding.tilNombres.getEditText().getText().toString().trim() : "";
        String email = binding.tilCorreo.getEditText() != null ? binding.tilCorreo.getEditText().getText().toString().trim() : "";
        String pass = binding.tilPassword.getEditText() != null ? binding.tilPassword.getEditText().getText().toString().trim() : "";
        String confirm = binding.tilConfirmPassword.getEditText() != null ? binding.tilConfirmPassword.getEditText().getText().toString().trim() : "";

        if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirm)) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.btnRegistrarse.setEnabled(false);
        ApiClient.getService().registrar(new RegisterRequest(nombre, email, pass)).enqueue(new Callback<ApiResponse<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Usuario>> call, @NonNull Response<ApiResponse<Usuario>> response) {
                if (isAdded()) {
                    binding.btnRegistrarse.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Usuario u = response.body().getData();
                        session.saveUser(u);
                        Toast.makeText(requireContext(), "¡Registro completado!", Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_navigation_registrar_to_navigation_inicio);
                    } else {
                        String msg = response.body() != null ? response.body().getMessage() : "Error en el registro";
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Usuario>> call, @NonNull Throwable t) {
                if (isAdded()) {
                    binding.btnRegistrarse.setEnabled(true);
                    Toast.makeText(requireContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
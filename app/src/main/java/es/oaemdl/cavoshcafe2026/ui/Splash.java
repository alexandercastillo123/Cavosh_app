package es.oaemdl.cavoshcafe2026.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import es.oaemdl.cavoshcafe2026.R;
import es.oaemdl.cavoshcafe2026.data.pref.SessionManager;
import es.oaemdl.cavoshcafe2026.databinding.FragmentSplashBinding;

public class Splash extends Fragment {
    private FragmentSplashBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        SessionManager session = new SessionManager(requireContext());

        binding.btnComenzar.setOnClickListener(v -> {
            if (session.isLoggedIn()) {
                navController.navigate(R.id.action_navigation_login_to_navigation_inicio);
            } else {
                navController.navigate(R.id.action_navigation_splash_to_navigation_login);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
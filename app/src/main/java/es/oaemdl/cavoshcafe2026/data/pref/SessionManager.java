package es.oaemdl.cavoshcafe2026.data.pref;

import android.content.Context;
import android.content.SharedPreferences;
import es.oaemdl.cavoshcafe2026.data.model.Sucursal;
import es.oaemdl.cavoshcafe2026.data.model.Usuario;

public class SessionManager {
    private static final String PREF_NAME = "cavosh_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_POINTS = "user_points";
    private static final String KEY_CAFE_ID = "cafe_id";
    private static final String KEY_CAFE_NAME = "cafe_name";
    private static final String KEY_CAFE_ADDRESS = "cafe_address";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(Usuario user) {
        prefs.edit()
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putInt(KEY_USER_ID, user.getIdUsuario())
                .putString(KEY_USER_NAME, user.getNombreCompleto())
                .putString(KEY_USER_EMAIL, user.getEmail())
                .putInt(KEY_USER_POINTS, user.getPuntos())
                .apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, 1);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "Laura Vat");
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "usuario@cavosh.com");
    }

    public int getUserPoints() {
        return prefs.getInt(KEY_USER_POINTS, 124);
    }

    public void updatePoints(int points) {
        prefs.edit().putInt(KEY_USER_POINTS, points).apply();
    }

    public void saveSelectedCafe(Sucursal cafe) {
        if (cafe != null) {
            prefs.edit()
                    .putInt(KEY_CAFE_ID, cafe.getIdSucursal())
                    .putString(KEY_CAFE_NAME, cafe.getNombre())
                    .putString(KEY_CAFE_ADDRESS, cafe.getDireccion())
                    .apply();
        }
    }

    public int getSelectedCafeId() {
        return prefs.getInt(KEY_CAFE_ID, 1);
    }

    public String getSelectedCafeName() {
        return prefs.getString(KEY_CAFE_NAME, "Cavosh Cafe");
    }

    public String getSelectedCafeAddress() {
        return prefs.getString(KEY_CAFE_ADDRESS, "Legnicka 20, Wroclaw");
    }

    public void logout() {
        prefs.edit().clear().apply();
    }
}

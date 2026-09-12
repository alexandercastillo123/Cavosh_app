package es.oaemdl.cavoshcafe2026.data.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("nombreCompleto")
    private String nombreCompleto;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public RegisterRequest(String nombreCompleto, String email, String password) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.password = password;
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}

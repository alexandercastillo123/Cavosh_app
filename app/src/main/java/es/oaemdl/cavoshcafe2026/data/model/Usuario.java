package es.oaemdl.cavoshcafe2026.data.model;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    @SerializedName("idUsuario")
    private int idUsuario;

    @SerializedName("nombreCompleto")
    private String nombreCompleto;

    @SerializedName("email")
    private String email;

    @SerializedName("puntos")
    private int puntos;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("recibirNotificaciones")
    private boolean recibirNotificaciones;

    @SerializedName("compartirUbicacion")
    private boolean compartirUbicacion;

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCompleto() { return nombreCompleto != null ? nombreCompleto : "Usuario"; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public boolean isRecibirNotificaciones() { return recibirNotificaciones; }
    public void setRecibirNotificaciones(boolean recibirNotificaciones) { this.recibirNotificaciones = recibirNotificaciones; }

    public boolean isCompartirUbicacion() { return compartirUbicacion; }
    public void setCompartirUbicacion(boolean compartirUbicacion) { this.compartirUbicacion = compartirUbicacion; }
}

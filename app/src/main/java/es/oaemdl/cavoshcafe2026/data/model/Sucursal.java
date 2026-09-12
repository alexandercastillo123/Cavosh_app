package es.oaemdl.cavoshcafe2026.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Sucursal implements Serializable {
    @SerializedName("idSucursal")
    private int idSucursal;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("ciudad")
    private String ciudad;

    @SerializedName("horarioAtencion")
    private String horarioAtencion;

    @SerializedName("latitud")
    private double latitud;

    @SerializedName("longitud")
    private double longitud;

    public int getIdSucursal() { return idSucursal; }
    public void setIdSucursal(int idSucursal) { this.idSucursal = idSucursal; }

    public String getNombre() { return nombre != null ? nombre : "Cavosh Cafe"; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion != null ? direccion : ""; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCiudad() { return ciudad != null ? ciudad : ""; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getHorarioAtencion() { return horarioAtencion != null ? horarioAtencion : "Open: 8:00 AM - 22:00 PM"; }
    public void setHorarioAtencion(String horarioAtencion) { this.horarioAtencion = horarioAtencion; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }
}

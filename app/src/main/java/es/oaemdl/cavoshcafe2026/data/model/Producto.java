package es.oaemdl.cavoshcafe2026.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Producto implements Serializable {
    @SerializedName("idProducto")
    private int idProducto;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("precio")
    private double precio;

    @SerializedName("categoria")
    private String categoria;

    @SerializedName("imagenUrl")
    private String imagenUrl;

    @SerializedName("esNuevo")
    private boolean esNuevo;

    @SerializedName("esFrecuente")
    private boolean esFrecuente;

    @SerializedName("tamanoMl")
    private int tamanoMl;

    // Transient UI helper
    private boolean favorito;

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre != null ? nombre : ""; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion != null ? descripcion : ""; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria != null ? categoria : ""; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public boolean isEsNuevo() { return esNuevo; }
    public void setEsNuevo(boolean esNuevo) { this.esNuevo = esNuevo; }

    public boolean isEsFrecuente() { return esFrecuente; }
    public void setEsFrecuente(boolean esFrecuente) { this.esFrecuente = esFrecuente; }

    public int getTamanoMl() { return tamanoMl; }
    public void setTamanoMl(int tamanoMl) { this.tamanoMl = tamanoMl; }

    public boolean isFavorito() { return favorito; }
    public void setFavorito(boolean favorito) { this.favorito = favorito; }
}

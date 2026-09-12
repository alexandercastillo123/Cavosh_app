package es.oaemdl.cavoshcafe2026.data.model;

import com.google.gson.annotations.SerializedName;

public class CuponResponse {
    @SerializedName("valido")
    private boolean valido;

    @SerializedName("codigo")
    private String codigo;

    @SerializedName("descuento")
    private double descuento;

    @SerializedName("nuevoTotal")
    private double nuevoTotal;

    @SerializedName("mensaje")
    private String mensaje;

    public boolean isValido() { return valido; }
    public void setValido(boolean valido) { this.valido = valido; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public double getNuevoTotal() { return nuevoTotal; }
    public void setNuevoTotal(double nuevoTotal) { this.nuevoTotal = nuevoTotal; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}

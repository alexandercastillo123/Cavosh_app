package es.oaemdl.cavoshcafe2026.data.model;

import com.google.gson.annotations.SerializedName;

public class CuponValidateRequest {
    @SerializedName("codigo")
    private String codigo;

    @SerializedName("subtotal")
    private double subtotal;

    public CuponValidateRequest(String codigo, double subtotal) {
        this.codigo = codigo;
        this.subtotal = subtotal;
    }

    public String getCodigo() { return codigo; }
    public double getSubtotal() { return subtotal; }
}

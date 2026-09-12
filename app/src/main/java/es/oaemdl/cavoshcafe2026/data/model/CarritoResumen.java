package es.oaemdl.cavoshcafe2026.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class CarritoResumen {
    @SerializedName("items")
    private List<CarritoItem> items = new ArrayList<>();

    @SerializedName("totalArticulos")
    private int totalArticulos;

    @SerializedName("subtotal")
    private double subtotal;

    @SerializedName("descuento")
    private double descuento;

    @SerializedName("total")
    private double total;

    @SerializedName("codigoCupon")
    private String codigoCupon;

    public List<CarritoItem> getItems() { return items != null ? items : new ArrayList<>(); }
    public void setItems(List<CarritoItem> items) { this.items = items; }

    public int getTotalArticulos() { return totalArticulos; }
    public void setTotalArticulos(int totalArticulos) { this.totalArticulos = totalArticulos; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getCodigoCupon() { return codigoCupon; }
    public void setCodigoCupon(String codigoCupon) { this.codigoCupon = codigoCupon; }
}

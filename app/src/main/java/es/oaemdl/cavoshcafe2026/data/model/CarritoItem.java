package es.oaemdl.cavoshcafe2026.data.model;

import com.google.gson.annotations.SerializedName;

public class CarritoItem {
    @SerializedName("idCarrito")
    private int idCarrito;

    @SerializedName("producto")
    private Producto producto;

    @SerializedName("cantidad")
    private int cantidad;

    @SerializedName("tamano")
    private String tamano;

    @SerializedName("tipoLeche")
    private String tipoLeche;

    @SerializedName("conCrema")
    private String conCrema;

    @SerializedName("conCafeina")
    private String conCafeina;

    @SerializedName("precioUnitario")
    private double precioUnitario;

    @SerializedName("subtotal")
    private double subtotal;

    public int getIdCarrito() { return idCarrito; }
    public void setIdCarrito(int idCarrito) { this.idCarrito = idCarrito; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getTamano() { return tamano != null ? tamano : "Small"; }
    public void setTamano(String tamano) { this.tamano = tamano; }

    public String getTipoLeche() { return tipoLeche != null ? tipoLeche : "Full-fat milk"; }
    public void setTipoLeche(String tipoLeche) { this.tipoLeche = tipoLeche; }

    public String getConCrema() { return conCrema != null ? conCrema : "Without whipped cream"; }
    public void setConCrema(String conCrema) { this.conCrema = conCrema; }

    public String getConCafeina() { return conCafeina != null ? conCafeina : "With caffeine"; }
    public void setConCafeina(String conCafeina) { this.conCafeina = conCafeina; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}

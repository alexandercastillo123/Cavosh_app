package es.oaemdl.cavoshcafe2026.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DetallePedido implements Serializable {
    @SerializedName("idDetalle")
    private int idDetalle;

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

    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }

    public String getTipoLeche() { return tipoLeche; }
    public void setTipoLeche(String tipoLeche) { this.tipoLeche = tipoLeche; }

    public String getConCrema() { return conCrema; }
    public void setConCrema(String conCrema) { this.conCrema = conCrema; }

    public String getConCafeina() { return conCafeina; }
    public void setConCafeina(String conCafeina) { this.conCafeina = conCafeina; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}

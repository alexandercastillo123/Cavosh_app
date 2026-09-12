package es.oaemdl.cavoshcafe2026.data.model;

import com.google.gson.annotations.SerializedName;

public class CarritoItemRequest {
    @SerializedName("idUsuario")
    private int idUsuario;

    @SerializedName("idProducto")
    private int idProducto;

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

    public CarritoItemRequest(int idUsuario, int idProducto, int cantidad, String tamano,
                              String tipoLeche, String conCrema, String conCafeina, double precioUnitario) {
        this.idUsuario = idUsuario;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.tamano = tamano;
        this.tipoLeche = tipoLeche;
        this.conCrema = conCrema;
        this.conCafeina = conCafeina;
        this.precioUnitario = precioUnitario;
    }

    public int getIdUsuario() { return idUsuario; }
    public int getIdProducto() { return idProducto; }
    public int getCantidad() { return cantidad; }
    public String getTamano() { return tamano; }
    public String getTipoLeche() { return tipoLeche; }
    public String getConCrema() { return conCrema; }
    public String getConCafeina() { return conCafeina; }
    public double getPrecioUnitario() { return precioUnitario; }
}

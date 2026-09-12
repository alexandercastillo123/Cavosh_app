package es.oaemdl.cavoshcafe2026.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Serializable {
    @SerializedName("idPedido")
    private int idPedido;

    @SerializedName("sucursal")
    private Sucursal sucursal;

    @SerializedName("numeroPedido")
    private String numeroPedido;

    @SerializedName("metodoEntrega")
    private String metodoEntrega;

    @SerializedName("fechaEntrega")
    private String fechaEntrega;

    @SerializedName("horaEntrega")
    private String horaEntrega;

    @SerializedName("metodoPago")
    private String metodoPago;

    @SerializedName("tarjetaUltimos4")
    private String tarjetaUltimos4;

    @SerializedName("subtotal")
    private double subtotal;

    @SerializedName("descuento")
    private double descuento;

    @SerializedName("total")
    private double total;

    @SerializedName("estado")
    private String estado;

    @SerializedName("detalles")
    private List<DetallePedido> detalles = new ArrayList<>();

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public Sucursal getSucursal() { return sucursal; }
    public void setSucursal(Sucursal sucursal) { this.sucursal = sucursal; }

    public String getNumeroPedido() { return numeroPedido != null ? numeroPedido : ""; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }

    public String getMetodoEntrega() { return metodoEntrega != null ? metodoEntrega : "PICKUP"; }
    public void setMetodoEntrega(String metodoEntrega) { this.metodoEntrega = metodoEntrega; }

    public String getFechaEntrega() { return fechaEntrega != null ? fechaEntrega : ""; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getHoraEntrega() { return horaEntrega != null ? horaEntrega : "08:00 AM"; }
    public void setHoraEntrega(String horaEntrega) { this.horaEntrega = horaEntrega; }

    public String getMetodoPago() { return metodoPago != null ? metodoPago : "CARD"; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getTarjetaUltimos4() { return tarjetaUltimos4 != null ? tarjetaUltimos4 : "2048"; }
    public void setTarjetaUltimos4(String tarjetaUltimos4) { this.tarjetaUltimos4 = tarjetaUltimos4; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado != null ? estado : "PLACED"; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<DetallePedido> getDetalles() { return detalles != null ? detalles : new ArrayList<>(); }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}

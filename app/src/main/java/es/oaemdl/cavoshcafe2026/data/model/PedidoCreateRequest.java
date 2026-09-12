package es.oaemdl.cavoshcafe2026.data.model;

import com.google.gson.annotations.SerializedName;

public class PedidoCreateRequest {
    @SerializedName("idUsuario")
    private int idUsuario;

    @SerializedName("idSucursal")
    private int idSucursal;

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

    @SerializedName("codigoCupon")
    private String codigoCupon;

    public PedidoCreateRequest(int idUsuario, int idSucursal, String metodoEntrega,
                               String fechaEntrega, String horaEntrega, String metodoPago,
                               String tarjetaUltimos4, String codigoCupon) {
        this.idUsuario = idUsuario;
        this.idSucursal = idSucursal;
        this.metodoEntrega = metodoEntrega;
        this.fechaEntrega = fechaEntrega;
        this.horaEntrega = horaEntrega;
        this.metodoPago = metodoPago;
        this.tarjetaUltimos4 = tarjetaUltimos4;
        this.codigoCupon = codigoCupon;
    }

    public int getIdUsuario() { return idUsuario; }
    public int getIdSucursal() { return idSucursal; }
    public String getMetodoEntrega() { return metodoEntrega; }
    public String getFechaEntrega() { return fechaEntrega; }
    public String getHoraEntrega() { return horaEntrega; }
    public String getMetodoPago() { return metodoPago; }
    public String getTarjetaUltimos4() { return tarjetaUltimos4; }
    public String getCodigoCupon() { return codigoCupon; }
}

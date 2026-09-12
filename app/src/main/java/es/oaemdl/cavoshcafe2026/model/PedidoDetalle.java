package es.oaemdl.cavoshcafe2026.model;

public class PedidoDetalle {
    int idPedido, idProducto, Cantidad;
    int Size, Milk, Cream, Caffeine;
    double Total;

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public int getMilk() {
        return Milk;
    }

    public void setMilk(int milk) {
        Milk = milk;
    }

    public int getCream() {
        return Cream;
    }

    public void setCream(int cream) {
        Cream = cream;
    }

    public int getCaffeine() {
        return Caffeine;
    }

    public void setCaffeine(int caffeine) {
        Caffeine = caffeine;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public boolean isCream() {
        return Cream == 1;
    }

    public boolean isCaffeine() {
        return Caffeine == 1;
    }

}

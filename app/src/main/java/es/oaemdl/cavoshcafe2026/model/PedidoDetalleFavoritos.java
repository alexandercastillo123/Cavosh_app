package es.oaemdl.cavoshcafe2026.model;

public class PedidoDetalleFavoritos {
    int idProducto, Size, Milk, Cream, Caffeine;

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
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

    public boolean isCream() {
        return Cream == 1;
    }

    public boolean isCaffeine() {
        return Caffeine == 1;
    }

}
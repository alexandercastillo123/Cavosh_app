package es.oaemdl.cavoshcafe2026.model;

public class Producto {
    int id, Categoria, Nuevo;
    String Detalle, Descripcion;
    double Precio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoria() {
        return Categoria;
    }

    public void setCategoria(int categoria) {
        Categoria = categoria;
    }

    public int getNuevo() {
        return Nuevo;
    }

    public void setNuevo(int nuevo) {
        Nuevo = nuevo;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String detalle) {
        Detalle = detalle;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }

    public boolean isNuevo() {
        return Nuevo == 1;
    }

}

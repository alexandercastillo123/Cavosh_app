package es.oaemdl.cavoshcafe2026.model;

public class Distrito {
    int id;
    String Detalle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String detalle) {
        Detalle = detalle;
    }

    public Distrito(int id, String detalle) {
        this.id = id;
        Detalle = detalle;
    }

    @Override
    public String toString() {
        return Detalle;
    }

}
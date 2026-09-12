package es.oaemdl.cavoshcafe2026.model;

public class Usuario {
    int id, Login;
    String Nombres, Correo, Passwordd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLogin() {
        return Login;
    }

    public void setLogin(int login) {
        this.Login = login;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getPasswordd() {
        return Passwordd;
    }

    public void setPasswordd(String passwordd) {
        Passwordd = passwordd;
    }

    public boolean isLogin() {
        return Login == 1;
    }

}

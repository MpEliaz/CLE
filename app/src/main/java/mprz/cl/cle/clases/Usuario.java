package mprz.cl.cle.clases;

/**
 * Created by elias on 28-09-15.
 */
public class Usuario {

    private String Nombre;
    private String Apellido;
    private String rut;
    private String contraseña;

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    // METODOS

    public void validar_usuario(){

    }
}

package mprz.cl.cle.clases;

/**
 * Created by elias on 26-10-15.
 */
public class Persona {

    private int id;
    private String rut;
    private String nombre;
    private String categoria;

    public Persona() {
    }

    public Persona(int id, String rut, String nombre, String categoria) {
        this.id = id;
        this.rut = rut;
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}

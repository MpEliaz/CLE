package mprz.cl.cle.clases;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elias on 26-10-15.
 */
public class Persona {

    private int id;
    private String rut;
    private String nombre;
    private String categoria;
    private String estado_encuesta;

    public Persona() {
    }

    public Persona(String rut, String nombre) {
        this.rut = rut;
        this.nombre = nombre;
    }

    public Persona(int id, String rut, String nombre, String categoria, String estado_encuesta) {
        this.id = id;
        this.rut = rut;
        this.nombre = nombre;
        this.categoria = categoria;
        this.estado_encuesta = estado_encuesta;
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

    public String getEstado_encuesta() {
        return estado_encuesta;
    }

    public void setEstado_encuesta(String estado_encuesta) {
        this.estado_encuesta = estado_encuesta;
    }

    public JSONObject getJsonObject(){

        JSONObject o = new JSONObject();
        try {
            o.put("id",getId());
            o.put("rut",getRut());
            o.put("nombre",getNombre());
            o.put("categoria",getCategoria());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return o;
    }
}

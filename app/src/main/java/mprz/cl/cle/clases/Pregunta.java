package mprz.cl.cle.clases;

import java.util.ArrayList;

/**
 * Created by elias on 01-11-15.
 */
public class Pregunta {

    private int id;
    private String titulo;
    private ArrayList<Respuesta> respuestas;

    public Pregunta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ArrayList<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(ArrayList<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}

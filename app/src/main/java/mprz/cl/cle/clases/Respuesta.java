package mprz.cl.cle.clases;

/**
 * Created by elias on 01-11-15.
 */
public class Respuesta {
    private int id;
    private String id_pregunta;
    private String cod_relacion;
    private String respuesta;

    public Respuesta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(String id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

    public String getCod_relacion() {
        return cod_relacion;
    }

    public void setCod_relacion(String cod_relacion) {
        this.cod_relacion = cod_relacion;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}

package mprz.cl.cle.clases;

/**
 * Created by elias on 01-11-15.
 */
public class Respuesta {
    private int id;
    private String id_texto;
    private String respuesta;

    public Respuesta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_texto() {
        return id_texto;
    }

    public void setId_texto(String id_texto) {
        this.id_texto = id_texto;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}

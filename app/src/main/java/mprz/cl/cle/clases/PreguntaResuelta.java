package mprz.cl.cle.clases;

/**
 * Created by elias on 19-01-16.
 */
public class PreguntaResuelta {

    private int id;
    private String run_evaluado;
    private String id_encuesta;
    private String id_pregunta;
    private int id_respuesta;

    public PreguntaResuelta(int id, String run_evaluado, String id_encuesta, String id_pregunta, int id_respuesta) {
        this.id = id;
        this.run_evaluado = run_evaluado;
        this.id_encuesta = id_encuesta;
        this.id_pregunta = id_pregunta;
        this.id_respuesta = id_respuesta;
    }

    public PreguntaResuelta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRun_evaluado() {
        return run_evaluado;
    }

    public void setRun_evaluado(String run_evaluado) {
        this.run_evaluado = run_evaluado;
    }

    public String getId_encuesta() {
        return id_encuesta;
    }

    public void setId_encuesta(String id_encuesta) {
        this.id_encuesta = id_encuesta;
    }

    public String getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(String id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

    public int getId_respuesta() {
        return id_respuesta;
    }

    public void setId_respuesta(int id_respuesta) {
        this.id_respuesta = id_respuesta;
    }
}

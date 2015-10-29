package mprz.cl.cle.clases;

/**
 * Created by Elias Millachine on 29-10-2015.
 */
public class Encuesta {

    private int id;
    private String runEvaluado;
    private String nombreEvaluado;
    private String runEvaluador;
    private int periodo;
    private String relacion;
    private String estado;

    public Encuesta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRunEvaluado() {
        return runEvaluado;
    }

    public void setRunEvaluado(String runEvaluado) {
        this.runEvaluado = runEvaluado;
    }

    public String getNombreEvaluado() {
        return nombreEvaluado;
    }

    public void setNombreEvaluado(String nombreEvaluado) {
        this.nombreEvaluado = nombreEvaluado;
    }

    public String getRunEvaluador() {
        return runEvaluador;
    }

    public void setRunEvaluador(String runEvaluador) {
        this.runEvaluador = runEvaluador;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public String getRelacion() {
        return relacion;
    }

    public void setRelacion(String relacion) {
        this.relacion = relacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

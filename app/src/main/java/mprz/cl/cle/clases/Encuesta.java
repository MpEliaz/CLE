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
    private int id_encuesta;
    private String cod_relacion;

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

    public int getId_encuesta() {
        return id_encuesta;
    }

    public void setId_encuesta(int id_encuesta) {
        this.id_encuesta = id_encuesta;
    }

    public String getCod_relacion() {
        return cod_relacion;
    }

    public void setCod_relacion(String cod_relacion) {
        this.cod_relacion = cod_relacion;
    }
}

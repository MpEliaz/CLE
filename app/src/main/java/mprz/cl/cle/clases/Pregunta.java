package mprz.cl.cle.clases;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elias on 01-11-15.
 */
public class Pregunta implements Parcelable{

    private int id;
    private String id_texto;
    private String titulo;
    private String run_evaluado;
    private ArrayList<Respuesta> respuestas;

    public Pregunta() {
    }

    public Pregunta(Parcel in) {
        readFromParcel(in);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(id_texto);
        parcel.writeString(titulo);
        parcel.writeList(respuestas);

    }

    private void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.id_texto = in.readString();
        this.titulo = in.readString();
        this.respuestas = in.readArrayList(null);
    }

    public static Creator<Pregunta> CREATOR = new Creator<Pregunta>() {
        public Pregunta createFromParcel(Parcel in) {
            return new Pregunta(in);
        }

        public Pregunta[] newArray(int size) {
            return new Pregunta[size];
        }
    };
}

package mprz.cl.cle.clases;

import java.util.ArrayList;

/**
 * Created by elias on 08-12-15.
 */
public class Documento {

    private int id;
    private String nombre;
    private ArrayList<String> paginas;

    public Documento() {
    }

    public Documento(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getPaginas() {
        return paginas;
    }

    public void setPaginas(ArrayList<String> paginas) {
        this.paginas = paginas;
    }

    public ArrayList<Documento> datos_ejemplo(){

        ArrayList<Documento> data = new ArrayList<>();

        Documento d1 = new Documento(1, "Atributos");
        Documento d2 = new Documento(2, "Competencias");
        Documento d3 = new Documento(3, "Herramientas");

        data.add(d1);
        data.add(d2);
        data.add(d3);

        return data;
    }
}

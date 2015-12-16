package mprz.cl.cle.clases;

import java.util.ArrayList;

/**
 * Created by elias on 08-12-15.
 */
public class Documento {

    private int id;
    private String nombre;
    private String[] paginas;

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

    public String[] getPaginas() {
        return paginas;
    }

    public void setPaginas(String[] paginas) {
        this.paginas = paginas;
    }

    public ArrayList<Documento> datos_ejemplo(){

        ArrayList<Documento> data = new ArrayList<>();

        Documento d1 = new Documento(1, "Atributos");
        d1.setPaginas(new String[]{"competencias_pag_01",
                "competencias_pag_02",
                "competencias_pag_03",
                "competencias_pag_04",
                "competencias_pag_05",
                "competencias_pag_06",
                "competencias_pag_07",
                "competencias_pag_08",
                "competencias_pag_09",
                "competencias_pag_10",
                "competencias_pag_11",
                "competencias_pag_12",
                "competencias_pag_13",
                "competencias_pag_14",
                "competencias_pag_15",
                "competencias_pag_16",
                "competencias_pag_17",
                "competencias_pag_18",
                "competencias_pag_19",
                "competencias_pag_20",
                "competencias_pag_21",
                "competencias_pag_22",
                "competencias_pag_23",
        });
        Documento d2 = new Documento(2, "Competencias");
        Documento d3 = new Documento(3, "Herramientas");

        data.add(d1);
        data.add(d2);
        data.add(d3);

        return data;
    }


}

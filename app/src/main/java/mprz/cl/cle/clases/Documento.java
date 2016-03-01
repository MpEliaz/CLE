package mprz.cl.cle.clases;

import java.util.ArrayList;

import mprz.cl.cle.R;

/**
 * Created by elias on 08-12-15.
 */
public class Documento {

    private int id;
    private String nombre;
    private int[] paginas;

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

    public int[] getPaginas() {
        return paginas;
    }

    public void setPaginas(int[] paginas) {
        this.paginas = paginas;
    }

    public ArrayList<Documento> datos_ejemplo(){

        ArrayList<Documento> data = new ArrayList<>();

        Documento d1 = new Documento(1, "Competencias");
        d1.setPaginas(new int[]{
                R.drawable.competencias_pag_01_,
                R.drawable.competencias_pag_02_,
                R.drawable.competencias_pag_03_,
                R.drawable.competencias_pag_04_,
                R.drawable.competencias_pag_05_,
                R.drawable.competencias_pag_06_,
                R.drawable.competencias_pag_07_,
                R.drawable.competencias_pag_08_,
                R.drawable.competencias_pag_09_,
                R.drawable.competencias_pag_10_,
                R.drawable.competencias_pag_11_,
                R.drawable.competencias_pag_12_,
                R.drawable.competencias_pag_13_,
                R.drawable.competencias_pag_14_,
                R.drawable.competencias_pag_15_,
                R.drawable.competencias_pag_16_,
                R.drawable.competencias_pag_17_,
                R.drawable.competencias_pag_18_,
                R.drawable.competencias_pag_19_,
                R.drawable.competencias_pag_20_,
                R.drawable.competencias_pag_21_,
                R.drawable.competencias_pag_22_,
                R.drawable.competencias_pag_23_,
        });


        data.add(d1);

        Documento d2 = new Documento(2, "Atributos");
        d2.setPaginas(new int[]{
                R.drawable.atributos_1,
                R.drawable.atributos_2,
                R.drawable.atributos_3,
                R.drawable.atributos_4,
                R.drawable.atributos_5,
                R.drawable.atributos_6,
                R.drawable.atributos_7,
                R.drawable.atributos_8,

        });
        data.add(d2);

        Documento d3 = new Documento(2, "Herramientas");
        d3.setPaginas(new int[]{
                R.drawable.herramientas_1,
                R.drawable.herramientas_2,
                R.drawable.herramientas_3,
                R.drawable.herramientas_4,
                R.drawable.herramientas_5,
                R.drawable.herramientas_6,
                R.drawable.herramientas_7,
                R.drawable.herramientas_8,
                R.drawable.herramientas_9,
                R.drawable.herramientas_10,
                R.drawable.herramientas_11,
                R.drawable.herramientas_12,
                R.drawable.herramientas_13,
                R.drawable.herramientas_14,
                R.drawable.herramientas_15,
                R.drawable.herramientas_16,
                R.drawable.herramientas_17,
                R.drawable.herramientas_18,
                R.drawable.herramientas_19,
                R.drawable.herramientas_20,
                R.drawable.herramientas_21,
                R.drawable.herramientas_22,
                R.drawable.herramientas_23,
                R.drawable.herramientas_24,


        });
        data.add(d3);

        return data;
    }


}

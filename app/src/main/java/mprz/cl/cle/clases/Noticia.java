package mprz.cl.cle.clases;

import java.util.ArrayList;

/**
 * Created by elias on 07-12-15.
 */
public class Noticia {

    private int id;
    private String titulo;
    private String cuerpo;
    private String url_imagen;

    public Noticia() {
    }

    public Noticia(int id, String titulo, String cuerpo, String url_imagen) {
        this.id = id;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.url_imagen = url_imagen;
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

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    public ArrayList<Noticia> datos_ejemplo(){

        ArrayList<Noticia> data = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Noticia n = new Noticia(i,"MILE EJECUTA TALLERES EN UNIDADES DE LA FUERZA TERRESTRE","Los integrantes del proyecto “Modelo Integral de Liderazgo del Ejército” (MILE), efectuaron talleres de desarrollo de liderazgo con las unidades de la Fuerza Terrestre, ubicadas en las ciudades de Temuco, Osorno y Valdivia.\n" +
                    "\n" +
                    "Estos talleres, que fueron dirigidos a oficiales y cuadro permanente, especialmente a aquellos que ejercen mando en las unidades, estuvieron orientados a desarrollar las competencias de liderazgo, como son la auto preparación, compromiso y el desarrollo de otros. Lo anterior, con el objeto de entregar y utilizar nuevas estrategias, metodologías y herramientas que sirvan de apoyo para autogestionarse y cómo ponerlo en práctica con el personal de su unidad. Estas actividades fueron acogidas positivamente por el personal, debido a que les otorgó la oportunidad de reflexionar sobre aspectos centrales de su vida personal y profesional, además de compartir experiencias de liderazgo y desarrollo de habilidades asociadas. Esta iniciativa continuará efectuándose en diferentes unidades de la Fuerza Terrestre hasta el mes de octubre.","url_"+i);
            data.add(n);
        }

        return data;
    }
}

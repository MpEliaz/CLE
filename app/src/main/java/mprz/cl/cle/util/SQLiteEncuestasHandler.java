package mprz.cl.cle.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.PreguntaResuelta;
import mprz.cl.cle.clases.Respuesta;

/**
 * Created by elias on 22-12-15.
 */
public class SQLiteEncuestasHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "CLE";
    private static final String DATABASE_NAME = "encuestas_db";

    private static final String TABLE_ENCUESTADOS = "ENCUESTADOS";
    private static final String TABLE_PREGUNTAS_ENCUESTA = "PREGUNTAS";
    private static final String TABLE_ENCUESTAS_TERMINADAS = "ENCUESTAS_TERMINADAS";
    private static final String TABLE_RESPUESTAS = "RESPUESTAS";
    private static final String TABLE_INTRO_ENCUESTAS = "INTRO_ENCUESTAS";
    private static final String TABLE_RESPUESTAS_ABIERTAS = "RESPUESTAS_ABIERTAS";


    private static final String CREATE_ENCUESTADOS_TABLE = "CREATE TABLE "+TABLE_ENCUESTADOS+" (id INTEGER PRIMARY KEY AUTOINCREMENT, id_encuesta INTEGER, runevaluado TEXT, nombreevaluado TEXT, relacion TEXT, cod_relacion TEXT, estado TEXT, terminado INTEGER)";
    private static final String CREATE_PREGUNTAS_TABLE = "CREATE TABLE "+TABLE_PREGUNTAS_ENCUESTA+" (id INTEGER PRIMARY KEY AUTOINCREMENT, cod_relacion TEXT, id_pregunta TEXT, pregunta TEXT)";
    private static final String CREATE_RESPUESTAS_TABLE = "CREATE TABLE "+TABLE_RESPUESTAS+" (id INTEGER PRIMARY KEY AUTOINCREMENT, cod_relacion TEXT, id_pregunta TEXT, id_respuesta INTEGER, respuesta TEXT)";
    private static final String CREATE_ENCUESTAS_TERMINADAS_TABLE = "CREATE TABLE "+TABLE_ENCUESTAS_TERMINADAS+" (id INTEGER PRIMARY KEY AUTOINCREMENT, run_evaluado TEXT, id_encuesta TEXT, id_pregunta TEXT, id_respuesta INTEGER)";
    private static final String CREATE_TEXTOS_ENCUESTA = "CREATE TABLE "+TABLE_INTRO_ENCUESTAS+" (id INTEGER PRIMARY KEY AUTOINCREMENT, cod_relacion TEXT, titulo_introduccion TEXT, introduccion TEXT, titulo_competencias TEXT, competencias TEXT, titulo_atributos TEXT, atributos TEXT)";
    private static final String CREATE_RESPUESTAS_ABIERTAS_TABLE = "CREATE TABLE "+TABLE_RESPUESTAS_ABIERTAS+" (id INTEGER PRIMARY KEY AUTOINCREMENT, run_evaluado TEXT, id_respuesta TEXT, respuesta TEXT)";


    public SQLiteEncuestasHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_ENCUESTADOS_TABLE);
        db.execSQL(CREATE_ENCUESTAS_TERMINADAS_TABLE);
        db.execSQL(CREATE_PREGUNTAS_TABLE);
        db.execSQL(CREATE_RESPUESTAS_TABLE);
        db.execSQL(CREATE_TEXTOS_ENCUESTA);
        db.execSQL(CREATE_RESPUESTAS_ABIERTAS_TABLE);
        Log.i("CLE", "bases de datos encuestas creada");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCUESTADOS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_PREGUNTAS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREGUNTAS_ENCUESTA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPUESTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTRO_ENCUESTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPUESTAS_ABIERTAS);

        onCreate(db);
    }

    public void guardarIntroduccionAEncuesta(String cod_relacion, String titulo_introduccion, String introduccion, String titulo_competencias, String competencias, String titulo_atributos, String atributos){
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT id FROM "+TABLE_INTRO_ENCUESTAS + " where cod_relacion='"+cod_relacion+"'";
        Cursor cursor = db.rawQuery(query, null);

        long id = -1;

        if(cursor != null){
            if(cursor.moveToFirst()){
                id = cursor.getInt(cursor.getColumnIndex("id"));
            }

            ContentValues values = new ContentValues();
            values.put("cod_relacion", cod_relacion);
            values.put("titulo_introduccion", titulo_introduccion);
            values.put("introduccion", introduccion);
            values.put("titulo_competencias", titulo_competencias);
            values.put("competencias", competencias);
            values.put("titulo_atributos", titulo_atributos);
            values.put("atributos", atributos);

            if(id == -1){

                long resp = db.insert(TABLE_INTRO_ENCUESTAS, null, values);
                Log.i(TAG, "nuevo registro de introduccion insertado con id: " + resp);

            }
            else{
                long resp = db.update(TABLE_INTRO_ENCUESTAS, values, "cod_relacion='"+cod_relacion+"'", null);
                Log.i(TAG, "nuevo registro de introduccion actualizado con id: " + resp);
            }

            db.close();
            cursor.close();
        }

    }

    public String[] obtenerIntroduccionAEncuesta(String cod_relacion){
        String[] datos = new String[7];
        String selectQuery = "SELECT  * FROM " + TABLE_INTRO_ENCUESTAS +" where cod_relacion='"+cod_relacion+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    datos[0] = cursor.getString(cursor.getColumnIndex("titulo_introduccion"));
                    datos[1] = cursor.getString(cursor.getColumnIndex("introduccion"));
                    datos[2] = cursor.getString(cursor.getColumnIndex("titulo_competencias"));
                    datos[3] = cursor.getString(cursor.getColumnIndex("competencias"));
                    datos[4] = cursor.getString(cursor.getColumnIndex("titulo_atributos"));
                    datos[5] = cursor.getString(cursor.getColumnIndex("atributos"));

                    Log.i(TAG, "intro obtenida desde bd");
                    // move to next row
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return datos;
    }

    public void guardarEncuestados(ArrayList<Encuesta> datos) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Encuesta e: datos) {

            ContentValues values = new ContentValues();
            values.put("runevaluado", e.getRunEvaluado());
            values.put("nombreevaluado", e.getNombreEvaluado());
            values.put("relacion", e.getRelacion());
            values.put("estado", e.getEstado());
            values.put("id_encuesta", e.getId_encuesta());
            values.put("cod_relacion", e.getCod_relacion());

            // Inserting Row
            long id = db.insert(TABLE_ENCUESTADOS, null, values);
            Log.i(TAG, "nuevo registro insertado con id: " + id);

        }
        db.close(); // Closing database connection
    }

    public ArrayList<Encuesta> ObtenerEncuestados() {
        ArrayList<Encuesta> encuestados = new ArrayList<Encuesta>();
        String selectQuery = "SELECT  * FROM " + TABLE_ENCUESTADOS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    Encuesta e = new Encuesta();
                    e.setId_encuesta(cursor.getInt(cursor.getColumnIndex("id_encuesta")));
                    e.setRunEvaluado(cursor.getString(cursor.getColumnIndex("runevaluado")));
                    e.setNombreEvaluado(cursor.getString(cursor.getColumnIndex("nombreevaluado")));
                    e.setRelacion(cursor.getString(cursor.getColumnIndex("relacion")));
                    e.setCod_relacion(cursor.getString(cursor.getColumnIndex("cod_relacion")));
                    e.setEstado(cursor.getString(cursor.getColumnIndex("estado")));

                    encuestados.add(e);
                    Log.i(TAG, "obtenido desde bd: " + e.getNombreEvaluado());
                    // move to next row
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return encuestados;
    }

    public void eliminarEncuestados() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ENCUESTADOS, null, null);
        db.close();

        Log.i(TAG, "tabla encuestados borrada");
    }

    public void saveQuestionWithAnswer(String id_pregunta, int id_respuesta, String cod_relacion, String run_evaluado) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("run_evaluado", run_evaluado);
        values.put("id_encuesta", cod_relacion);
        values.put("id_respuesta", id_respuesta);


        if (comprobarRespuestaEnBd(id_pregunta, cod_relacion).equals("")) {

            // Inserting Row
            values.put("id_pregunta", id_pregunta); // id_pregunta
            long id = db.insert(TABLE_ENCUESTAS_TERMINADAS, null, values);
            Log.i(TAG, "Nueva pregunta con respuesta insertada: run evaluado:"+run_evaluado+" ,id:" + id + ", id_encuesta:'"+cod_relacion+"', id_pregunta:'" + id_pregunta + "', id_respuesta:" + id_respuesta);
        }
        else
        {
            long id = db.update(TABLE_ENCUESTAS_TERMINADAS, values, "id_pregunta='"+id_pregunta+"' and run_evaluado='"+run_evaluado+"'", null);
            Log.i(TAG, "pregunta con respuesta actualizada: run evaluado:"+run_evaluado+", id:" + id + ", id_encuesta:'"+cod_relacion+"' id_pregunta:'" + id_pregunta + "', id_respuesta:" + id_respuesta);
        }

        if (db != null && db.isOpen()) {
            db.close();
        }
    }

/*    public void guardarRespuestaAbierta(String run_evaluado, String id_respuesta, String respuesta) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("run_evaluado", run_evaluado);
        values.put("id_encuesta", id_respuesta);
        values.put("id_respuesta", id_respuesta);


        if (comprobarRespuestaEnBd(id_pregunta, cod_relacion).equals("")) {

            // Inserting Row
            values.put("id_pregunta", id_pregunta); // id_pregunta
            long id = db.insert(TABLE_ENCUESTAS_TERMINADAS, null, values);
            Log.i(TAG, "Nueva pregunta con respuesta insertada: id:" + id + ", id_encuesta:'"+cod_relacion+"', id_pregunta:'" + id_pregunta + "', id_respuesta:" + id_respuesta);
        }
        else
        {
            long id = db.update(TABLE_ENCUESTAS_TERMINADAS, values, "id_pregunta='"+id_pregunta+"'", null);
            Log.i(TAG, "pregunta con respuesta actualizada: id:" + id + ", id_encuesta:'"+cod_relacion+"' id_pregunta:'" + id_pregunta + "', id_respuesta:" + id_respuesta);
        }

        if (db != null && db.isOpen()) {
            db.close();
        }
    }*/

    public ArrayList<PreguntaResuelta> ObtenerProgresoEncuesta(String run_evaluado, String cod_relacion) {
        ArrayList<PreguntaResuelta> progreso = new ArrayList<PreguntaResuelta>();
        String selectQuery = "SELECT  * FROM " + TABLE_ENCUESTAS_TERMINADAS+" where run_evaluado='"+run_evaluado+"' and id_encuesta='"+cod_relacion+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    PreguntaResuelta p = new PreguntaResuelta();

                    p.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    p.setRun_evaluado(cursor.getString(cursor.getColumnIndex("run_evaluado")));
                    p.setId_encuesta(cursor.getString(cursor.getColumnIndex("id_encuesta")));
                    p.setId_pregunta(cursor.getString(cursor.getColumnIndex("id_pregunta")));
                    p.setId_respuesta(cursor.getInt(cursor.getColumnIndex("id_respuesta")));


                    progreso.add(p);
                    Log.i(TAG, "pregunta resuelta id:"+p.getId()+", run: "+p.getRun_evaluado()+", id_encuesta:"+p.getId_encuesta()+", id_pregunta:"+p.getId_pregunta()+", id_respuesta:"+p.getId_respuesta());
                    // move to next row
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return progreso;
    }

    public boolean ComprobarPreguntaResuelta(String run_evaluado, String cod_relacion, String id_pregunta) {
        String selectQuery = "SELECT  * FROM " + TABLE_ENCUESTAS_TERMINADAS+" where run_evaluado='"+run_evaluado+"' and id_encuesta='"+cod_relacion+"' and id_pregunta='"+id_pregunta+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        PreguntaResuelta p = null;

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {

                    p = new PreguntaResuelta();

                    p.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    p.setRun_evaluado(cursor.getString(cursor.getColumnIndex("run_evaluado")));
                    p.setId_encuesta(cursor.getString(cursor.getColumnIndex("id_encuesta")));
                    p.setId_pregunta(cursor.getString(cursor.getColumnIndex("id_pregunta")));
                    p.setId_respuesta(cursor.getInt(cursor.getColumnIndex("id_respuesta")));

                    Log.i(TAG, "pregunta resuelta id:"+p.getId()+", run: "+p.getRun_evaluado()+", id_encuesta:"+p.getId_encuesta()+", id_pregunta:"+p.getId_pregunta()+", id_respuesta:"+p.getId_respuesta());
                    // move to next row
            }
            cursor.close();
        }
        db.close();
        return p != null;
    }
    public String comprobarRespuestaEnBd(String id, String cod_relacion) {
        String selectQuery = "SELECT id_pregunta FROM " + TABLE_ENCUESTAS_TERMINADAS + " where id_pregunta='"+ id+"' and id_encuesta='"+cod_relacion+"'";
        String result = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    result = cursor.getString(cursor.getColumnIndex("id_pregunta"));
                    Log.i(TAG, "obtenido desde bd: " + result);
                    // move to next row
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

/*        if (db != null && db.isOpen()) {
            db.close();
        }*/
        return result;
    }

    public JSONObject ObtenerEncuestaResuelta(String run_evaluado, String run_evaluador) {
        String selectQuery = "SELECT * FROM " + TABLE_ENCUESTAS_TERMINADAS + " where run_evaluado='"+run_evaluado+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<PreguntaResuelta> resueltas = new ArrayList<>();
        JSONObject obj = new JSONObject();

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {

                try {

                    obj.put("run_evaluador", run_evaluador);
                    obj.put("run_evaluado", cursor.getString(cursor.getColumnIndex("run_evaluado")));
                    JSONArray array = new JSONArray();
                    do {
                        JSONObject o = new JSONObject();
                        o.put("cod_pregunta",cursor.getString(cursor.getColumnIndex("id_pregunta")));
                        o.put("cod_respuesta",cursor.getInt(cursor.getColumnIndex("id_respuesta")));
                        array.put(o);

                    } while (cursor.moveToNext());
                    obj.put("respuestas", array);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        if (db != null && db.isOpen()) {
            db.close();
        }

        return obj;
    }

    public void eliminarRespuestasContestadas() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ENCUESTAS_TERMINADAS, null, null);
        db.close();

        Log.i(TAG, "respuestas contestadas eliminadas desde bd");
    }

    public void eliminarPreguntas() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_PREGUNTAS_ENCUESTA, null, null);
        db.delete(TABLE_RESPUESTAS,null,null);
        db.close();

        Log.i(TAG, "preguntas eliminadas desde bd");
        Log.i(TAG, "respuestas eliminadas desde bd");
    }

    public ArrayList<Pregunta> ObtenerEncuestaFromDB(String cod_relacion) {
        ArrayList<Pregunta> lista_preguntas = new ArrayList<Pregunta>();
        String selectQuery = "SELECT  * FROM " + TABLE_PREGUNTAS_ENCUESTA +" where cod_relacion='"+cod_relacion+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    Pregunta p = new Pregunta();
                    p.setId_texto(cursor.getString(cursor.getColumnIndex("id_pregunta")));
                    p.setTitulo(cursor.getString(cursor.getColumnIndex("pregunta")));

                    Cursor preg = db.rawQuery("SELECT * FROM "+TABLE_RESPUESTAS+" where cod_relacion='"+cod_relacion+"' and id_pregunta='"+p.getId_texto()+"'", null);
                    ArrayList<Respuesta> respuestas = new ArrayList<>();
                    if(preg != null){
                        if(preg.moveToFirst()){
                            do {

                                Respuesta r = new Respuesta();
                                r.setId(preg.getInt(preg.getColumnIndex("id_respuesta")));
                                r.setRespuesta(preg.getString(preg.getColumnIndex("respuesta")));
                                respuestas.add(r);
                            }while (preg.moveToNext());
                        }
                    }
                    p.setRespuestas(respuestas);
                    preg.close();
                    lista_preguntas.add(p);
                    Log.i(TAG, "pregunta obtenida: " + p.getId_texto());
                    // move to next row
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return lista_preguntas;
    }

    public void guardarEncuesta(ArrayList<Pregunta> datos, String cod_relacion) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Pregunta p: datos) {

            ContentValues values = new ContentValues();
            values.put("cod_relacion", cod_relacion);
            values.put("id_pregunta", p.getId_texto());
            values.put("pregunta", p.getTitulo());

            // Inserting Row
            long id = db.insert(TABLE_PREGUNTAS_ENCUESTA, null, values);
            Log.i(TAG, "nueva pregunta con id: "+p.getId_texto()+" de la encuesta: "+cod_relacion);
            for (Respuesta r: p.getRespuestas()) {
                ContentValues data = new ContentValues();
                data.put("cod_relacion", cod_relacion);
                data.put("id_pregunta", p.getId_texto());
                data.put("id_respuesta", r.getId());
                data.put("respuesta", r.getRespuesta());

                long id_p = db.insert(TABLE_RESPUESTAS, null, data);
                Log.i(TAG, "---> nueva respuesta: "+r.getId()+" de la pregunta:"+p.getId_texto()+" de la encuesta: "+cod_relacion);
            }


        }
        db.close(); // Closing database connection
    }

    public void recrearTablas() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCUESTADOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREGUNTAS_ENCUESTA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCUESTAS_TERMINADAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPUESTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTRO_ENCUESTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPUESTAS_ABIERTAS);

        db.execSQL(CREATE_ENCUESTADOS_TABLE);
        db.execSQL(CREATE_ENCUESTAS_TERMINADAS_TABLE);
        db.execSQL(CREATE_PREGUNTAS_TABLE);
        db.execSQL(CREATE_RESPUESTAS_TABLE);
        db.execSQL(CREATE_TEXTOS_ENCUESTA);
        db.execSQL(CREATE_RESPUESTAS_ABIERTAS_TABLE);

        Log.i(TAG, "tablas encuestados recreadas");
    }
}

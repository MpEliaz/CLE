package mprz.cl.cle.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.Respuesta;

/**
 * Created by elias on 22-12-15.
 */
public class SQLiteEncuestasHandler extends SQLiteHandler {

    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "CLE";
    private static final String DATABASE_NAME = "android_api";

    private static final String TABLE_ENCUESTADOS = "ENCUESTADOS";
    private static final String TABLE_ENCUESTAS = "ENCUESTAS";
    private static final String TABLE_ENCUESTAS_TERMINADAS = "ENCUESTAS_TERMINADAS";
    private static final String TABLE_RESPUESTAS = "RESPUESTAS";

    private static final String CREATE_ENCUESTADOS_TABLE = "CREATE TABLE "+TABLE_ENCUESTADOS+" (id_encuesta INTEGER PRIMARY KEY, runevaluado TEXT, nombreevaluado TEXT, relacion TEXT, estado TEXT, terminado INTEGER)";
    private static final String CREATE_ENCUESTAS_TERMINADAS_TABLE = "CREATE TABLE "+TABLE_ENCUESTAS_TERMINADAS+" (id INTEGER PRIMARY KEY AUTOINCREMENT, id_encuesta INTEGER, id_pregunta INTEGER, id_respuesta INTEGER)";
    private static final String CREATE_ENCUESTAS_TABLE = "CREATE TABLE "+TABLE_ENCUESTAS+" (id INTEGER PRIMARY KEY AUTOINCREMENT, id_encuesta INTEGER, id_pregunta INTEGER, pregunta TEXT)";
    private static final String CREATE_RESPUESTAS_TABLE = "CREATE TABLE "+TABLE_RESPUESTAS+" (id INTEGER PRIMARY KEY AUTOINCREMENT, id_encuesta INTEGER, id_pregunta INTEGER, id_respuesta INTEGER, respuesta TEXT)";

    public SQLiteEncuestasHandler(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);

        db.execSQL(CREATE_ENCUESTADOS_TABLE);
        db.execSQL(CREATE_ENCUESTAS_TERMINADAS_TABLE);
        db.execSQL(CREATE_ENCUESTAS_TABLE);
        db.execSQL(CREATE_RESPUESTAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCUESTADOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCUESTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCUESTAS_TERMINADAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPUESTAS);
    }

    public void guardarEncuestados(ArrayList<Encuesta> datos) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Encuesta e: datos) {

            ContentValues values = new ContentValues();
            //values.put("id_encuesta", e.getId());
            values.put("runevaluado", e.getRunEvaluado());
            values.put("nombreevaluado", e.getNombreEvaluado());
            values.put("relacion", e.getRelacion());
            values.put("estado", e.getEstado());

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
                    e.setId(cursor.getInt(cursor.getColumnIndex("id_encuesta")));
                    e.setRunEvaluado(cursor.getString(cursor.getColumnIndex("runevaluado")));
                    e.setNombreEvaluado(cursor.getString(cursor.getColumnIndex("nombreevaluado")));
                    e.setRelacion(cursor.getString(cursor.getColumnIndex("relacion")));
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

    public void saveQuestionWithAnswer(int id_pregunta, int id_respuesta) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_respuesta", id_respuesta); // id_respuesta
        values.put("id_encuesta", 1);

        if (comprobarRespuestaEnBd(id_pregunta) == -1) {

            // Inserting Row
            values.put("id_pregunta", id_pregunta); // id_pregunta
            long id = db.insert(TABLE_ENCUESTAS_TERMINADAS, null, values);
            Log.i(TAG, "Nueva pregunta con respuesta insertada: id:" + id + ", id_pregunta:" + id_pregunta + ", id_respuesta:" + id_respuesta);
        }
        else
        {
            long id = db.update(TABLE_ENCUESTAS_TERMINADAS, values, "id_pregunta="+id_pregunta, null);
            Log.i(TAG, "pregunta con respuesta actualizada: id:" + id + ", id_pregunta:" + id_pregunta + ", id_respuesta:" + id_respuesta);
        }

        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public int comprobarRespuestaEnBd(int id) {
        String selectQuery = "SELECT id_pregunta FROM " + TABLE_ENCUESTAS_TERMINADAS + " where id_pregunta="+ id;
        int result = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    result = cursor.getInt(cursor.getColumnIndex("id_pregunta"));
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
        db.delete(TABLE_ENCUESTAS, null, null);
        db.delete(TABLE_RESPUESTAS,null,null);
        db.close();

        Log.i(TAG, "preguntas eliminadas desde bd");
        Log.i(TAG, "respuestas eliminadas desde bd");
    }

    public ArrayList<Pregunta> ObtenerEncuestaFromDB() {
        ArrayList<Pregunta> lista_preguntas = new ArrayList<Pregunta>();
        String selectQuery = "SELECT  * FROM " + TABLE_ENCUESTAS +" where id_encuesta=1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    Pregunta p = new Pregunta();
                    p.setId(cursor.getInt(cursor.getColumnIndex("id_pregunta")));
                    p.setTitulo(cursor.getString(cursor.getColumnIndex("pregunta")));

                    Cursor preg = db.rawQuery("SELECT * FROM "+TABLE_RESPUESTAS+" where id_encuesta=1 and id_pregunta="+p.getId(), null);
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
                    Log.i(TAG, "pregunta obtenida: " + p.getId());
                    // move to next row
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return lista_preguntas;
    }

    public void guardarEncuesta(ArrayList<Pregunta> datos, int id_encuesta) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Pregunta p: datos) {

            ContentValues values = new ContentValues();
            values.put("id_encuesta", id_encuesta);
            values.put("id_pregunta", p.getId());
            values.put("pregunta", p.getTitulo());

            // Inserting Row
            long id = db.insert(TABLE_ENCUESTAS, null, values);

            for (Respuesta r: p.getRespuestas()) {
                ContentValues data = new ContentValues();
                data.put("id_encuesta", id_encuesta);
                data.put("id_pregunta", p.getId());
                data.put("id_respuesta", r.getId());
                data.put("respuesta", r.getRespuesta());

                long id_p = db.insert(TABLE_RESPUESTAS, null, data);
                Log.i(TAG, "nueva respuesta: "+r.getId()+" de la pregunta:"+p.getId()+" de la encuesta: "+id_encuesta);
            }


        }
        db.close(); // Closing database connection
    }

    public void recrearTablas() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCUESTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPUESTAS);

        db.execSQL(CREATE_ENCUESTAS_TABLE);
        db.execSQL(CREATE_RESPUESTAS_TABLE);

        Log.i(TAG, "tablas recreadas");
    }
}
package mprz.cl.cle.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Noticia;
import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.Respuesta;

/**
 * Created by elias on 14-10-15.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = "CLE";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "USUARIO";
    private static final String TABLE_ENCUESTADOS = "ENCUESTADOS";
    private static final String TABLE_ENCUESTAS = "ENCUESTAS";
    private static final String TABLE_ENCUESTAS_TERMINADAS = "ENCUESTAS_TERMINADAS";
    private static final String TABLE_RESPUESTAS = "RESPUESTAS";
    private static final String TABLE_NOTICIAS = "NOTICIAS";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_PATERNO = "paterno";
    private static final String KEY_MATERNO = "materno";
    private static final String CREATE_USER_TABLE = "CREATE TABLE "+TABLE_USER+" (id INTEGER PRIMARY KEY,nombre TEXT, paterno TEXT, materno TEXT)";
    private static final String CREATE_ENCUESTADOS_TABLE = "CREATE TABLE "+TABLE_ENCUESTADOS+" (id_encuesta INTEGER PRIMARY KEY, runevaluado TEXT, nombreevaluado TEXT, relacion TEXT, estado TEXT, terminado INTEGER)";
    private static final String CREATE_ENCUESTAS_TERMINADAS_TABLE = "CREATE TABLE "+TABLE_ENCUESTAS_TERMINADAS+" (id INTEGER PRIMARY KEY AUTOINCREMENT, id_encuesta INTEGER, id_pregunta INTEGER, id_respuesta INTEGER)";
    private static final String CREATE_ENCUESTAS_TABLE = "CREATE TABLE "+TABLE_ENCUESTAS+" (id INTEGER PRIMARY KEY AUTOINCREMENT, id_encuesta INTEGER, id_pregunta INTEGER, pregunta TEXT)";
    private static final String CREATE_RESPUESTAS_TABLE = "CREATE TABLE "+TABLE_RESPUESTAS+" (id INTEGER PRIMARY KEY AUTOINCREMENT, id_encuesta INTEGER, id_pregunta INTEGER, id_respuesta INTEGER, respuesta TEXT)";
    private static final String CREATE_NOTICIAS_TABLE = "CREATE TABLE NOTICIAS (id INTEGER PRIMARY KEY, usuario TEXT, titulo TEXT, resumen TEXT, completa TEXT, imagen TEXT)";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_ENCUESTADOS_TABLE);
        db.execSQL(CREATE_ENCUESTAS_TERMINADAS_TABLE);
        db.execSQL(CREATE_ENCUESTAS_TABLE);
        db.execSQL(CREATE_RESPUESTAS_TABLE);
        db.execSQL(CREATE_NOTICIAS_TABLE);

        Log.i(TAG, "base de datos creada");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCUESTADOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCUESTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCUESTAS_TERMINADAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPUESTAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICIAS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void guardarUsuario(String nombres, String paterno, String materno) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, nombres); // Name
        values.put(KEY_PATERNO, paterno); // Email
        values.put(KEY_MATERNO, materno); // Email

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.i(TAG, "New user inserted into sqlite: " + id);
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public void eliminarUsuario() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.i(TAG, "Deleted all user info from sqlite");
    }
    /**
     * Re crate database Delete all tables and create them again
     * */
    public boolean verificarUsuarioLogeado() {
        String query = "select * from USUARIO where id=1";
        int id;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            id = cursor.getInt(0);
        }
        else {
            return false;
        }

        if(id == 1){
            return true;
        }

        cursor.close();
        db.close();
        Log.i(TAG, "usuario verificado");
        return false;
    }

    /**
     * Storing encuestados in database
     * */
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

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("nombre", cursor.getString(1));
            user.put("paterno", cursor.getString(2));
            user.put("materno", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        Log.i(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }
    /**
     * guarda encuesta respondida en base de datos
     * */
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

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void eliminarRespuestasContestadas() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ENCUESTAS_TERMINADAS, null, null);
        db.close();

        Log.i(TAG, "respuestas contestadas eliminadas desde bd");
    }

       /**
     * Eliminar todas las preguntas guardadas en bd
     * */
    public void eliminarPreguntas() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ENCUESTAS, null, null);
        db.delete(TABLE_RESPUESTAS,null,null);
        db.close();

        Log.i(TAG, "preguntas eliminadas desde bd");
        Log.i(TAG, "respuestas eliminadas desde bd");
    }

    /**
     * Obtener la encuesta guardada en db
     * */
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

    /**
     * Guardar la encuesta en la base de datos para consultarla offline
     * */
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

    public void guardarNoticias(ArrayList<Noticia> noticias){

        SQLiteDatabase db = this.getWritableDatabase();

        for (Noticia n: noticias) {

            ContentValues values = new ContentValues();
            values.put("id", n.getId());
            values.put("usuario", n.getUsuario());
            values.put("titulo", n.getTitulo());
            values.put("resumen", n.getResumen());
            values.put("completa", n.getCuerpo());
            values.put("imagen", n.getUrl_imagen());

            // Inserting Row
            long id = db.insert(TABLE_NOTICIAS, null, values);
            Log.i(TAG, "Noticia guardada con id: "+id);
        }
        Log.i(TAG, "Noticias guardadas");
        db.close(); // Closing database connection

    }

    public void eliminarNoticias(){

        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_NOTICIAS, null, null);
        db.close();

        Log.i(TAG, "noticias eliminadas desde bd");

    }

    public ArrayList<Noticia> obtenerNoticias() {
        ArrayList<Noticia> noticias = new ArrayList<Noticia>();
        String selectQuery = "SELECT  * FROM " + TABLE_NOTICIAS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    Noticia n = new Noticia();
                    n.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    n.setUsuario(cursor.getString(cursor.getColumnIndex("usuario")));
                    n.setTitulo(cursor.getString(cursor.getColumnIndex("titulo")));
                    n.setResumen(cursor.getString(cursor.getColumnIndex("resumen")));
                    n.setCuerpo(cursor.getString(cursor.getColumnIndex("completa")));
                    n.setUrl_imagen(cursor.getString(cursor.getColumnIndex("imagen")));

                    noticias.add(n);
                    Log.i(TAG, "noticias obtenidas desde bd");
                    // move to next row
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return noticias;
    }
}

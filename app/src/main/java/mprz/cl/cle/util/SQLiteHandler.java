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
import mprz.cl.cle.clases.Persona;
import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.Respuesta;

/**
 * Created by elias on 14-10-15.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = "CLE";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ec_db";

    // Login table name
    private static final String TABLE_USER = "USUARIO";
    private static final String TABLE_NOTICIAS = "NOTICIAS";
    private static final String TABLE_MIS_EVALUADORES = "MIS_EVALUADORES";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_RUT = "rut";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_PATERNO = "paterno";
    private static final String KEY_MATERNO = "materno";
    private static final String CREATE_USER_TABLE = "CREATE TABLE "+TABLE_USER+" (id INTEGER PRIMARY KEY,rut TEXT, nombre TEXT, paterno TEXT, materno TEXT)";

    private static final String CREATE_NOTICIAS_TABLE = "CREATE TABLE NOTICIAS (id INTEGER PRIMARY KEY, usuario TEXT, titulo TEXT, resumen TEXT, completa TEXT, imagen TEXT)";
    private static final String CREATE_MIS_EVALUADORES = "CREATE TABLE "+TABLE_MIS_EVALUADORES+" (id INTEGER PRIMARY KEY AUTOINCREMENT, rut TEXT, nombre TEXT, relacion INTEGER)";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_NOTICIAS_TABLE);
        db.execSQL(CREATE_MIS_EVALUADORES);

        Log.i(TAG, "base de datos creada");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICIAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MIS_EVALUADORES);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void guardarUsuario(String rut, String nombres, String paterno, String materno) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RUT, rut); // Rut
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
        String selectQuery = "SELECT  * FROM " + TABLE_NOTICIAS + " order by id desc";

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

    public Noticia obtenerNoticia(int id) {

        String selectQuery = "SELECT  * FROM " + TABLE_NOTICIAS+" where id="+id;

        Noticia n;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                    n = new Noticia();
                    n.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    n.setUsuario(cursor.getString(cursor.getColumnIndex("usuario")));
                    n.setTitulo(cursor.getString(cursor.getColumnIndex("titulo")));
                    n.setResumen(cursor.getString(cursor.getColumnIndex("resumen")));
                    n.setCuerpo(cursor.getString(cursor.getColumnIndex("completa")));
                    n.setUrl_imagen(cursor.getString(cursor.getColumnIndex("imagen")));

                    Log.i(TAG, "noticias obtenida desde bd");
                return n;
            }
            cursor.close();
        }
        db.close();
        return null;
    }

    public void guardarMisEvaluadores(ArrayList<Persona> evaluadores){

        SQLiteDatabase db = this.getWritableDatabase();

        for (Persona p: evaluadores) {

            ContentValues values = new ContentValues();
            values.put("rut", p.getRut());
            values.put("titulo", p.getNombre());

            // Inserting Row
            long id = db.insert(TABLE_NOTICIAS, null, values);
            Log.i(TAG, "Mi evaluador guardado con id: "+id);
        }
        Log.i(TAG, "Todos mis evaluadores han sido guardados");
        db.close(); // Closing database connection

    }

    public void guardarEvaluador(String rut, String nombre, int relacion) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("rut", rut);
        values.put("nombre", nombre);
        values.put("relacion", relacion);

        // Inserting Row
        long id = db.insert(TABLE_MIS_EVALUADORES, null, values);
        db.close(); // Closing database connection

        Log.i(TAG, "Nuevo evaluador guardado con id: " + id);
    }


    public ArrayList<Persona> obtenerMisEvaluadores() {

        String selectQuery = "SELECT  * FROM " + TABLE_MIS_EVALUADORES;

        ArrayList<Persona> personas;
        Persona p;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            personas = new ArrayList<>();
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    p = new Persona();
                    p.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    p.setRut(cursor.getString(cursor.getColumnIndex("rut")));
                    p.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                    p.setCategoria(nombre_evaluador(cursor.getInt(cursor.getColumnIndex("relacion"))));

                    personas.add(p);
                }while (cursor.moveToNext());
                Log.i(TAG, "mis evaluadores obtenidos desde la db");
            }
            cursor.close();
            return personas;
        }
        db.close();
        return null;
    }

    public Persona obtenerEvaluador(String rut) {

        String selectQuery = "SELECT  * FROM " + TABLE_MIS_EVALUADORES+" where rut='"+rut+"'";

        Persona p;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                p = new Persona();
                p.setId(cursor.getInt(cursor.getColumnIndex("id")));
                p.setRut(cursor.getString(cursor.getColumnIndex("rut")));
                p.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));

                Log.i(TAG, "Evaluador obtenido desde la bd");
                return p;
            }
            cursor.close();
        }
        db.close();
        return null;
    }

    public void eliminarEvaluadores() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_MIS_EVALUADORES, null, null);
        db.close();

        Log.i(TAG, "Todos los evaluadores fueron eliminados");
    }

    public void eliminarEvaluador(String rut) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_MIS_EVALUADORES, "rut='"+rut+"'", null);
        db.close();

        Log.i(TAG, "El evaluador de rut: "+rut+" ah sido eliminado");
    }

    public void recrearTablas() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTICIAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MIS_EVALUADORES);

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_NOTICIAS_TABLE);
        db.execSQL(CREATE_MIS_EVALUADORES);

        Log.i(TAG, "tablas recreadas");
    }

    //obtiene la relacion a traves del id
    private String nombre_evaluador(int cod){
        switch (cod){
            case 1:
                return "Superior";
            case 2:
                return "Par";
            case 3:
                return "Subalterno";
        }
        return "";
    }
}

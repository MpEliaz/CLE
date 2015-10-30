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
import mprz.cl.cle.clases.Persona;

/**
 * Created by elias on 14-10-15.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = "CLE";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "USUARIO";
    private static final String TABLE_ENCUESTADOS = "ENCUESTADOS";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_PATERNO = "paterno";
    private static final String KEY_MATERNO = "materno";
    private static final String CREATE_USER_TABLE = "CREATE TABLE USUARIO (id INTEGER PRIMARY KEY," +
            "nombre TEXT, paterno TEXT, materno TEXT)";

    private static final String CREATE_ENCUESTADOS_TABLE = "CREATE TABLE ENCUESTADOS (id_encuesta INTEGER PRIMARY KEY," +
            "runevaluado TEXT, nombreevaluado TEXT, relacion TEXT, estado TEXT)";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_ENCUESTADOS_TABLE);

        Log.i(TAG, "base de datos creada");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCUESTADOS);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String nombres, String paterno, String materno) {
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
        }
        cursor.close();
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
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.i(TAG, "Deleted all user info from sqlite");
    }

}

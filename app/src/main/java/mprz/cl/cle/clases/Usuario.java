package mprz.cl.cle.clases;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

/**
 * Created by elias on 28-09-15.
 */
public class Usuario {

    private String Nombre;
    private String Apellido;
    private String rut;
    private String contraseña;
    private SQLiteDatabase db;


    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Usuario() {

    }

    // METODOS

    public boolean validar_usuario(){

        return true;
    }

    static SharedPreferences getSharedPreferences(Context cx){
        return PreferenceManager.getDefaultSharedPreferences(cx);
    }

    public static void setUserName(Context cx, String username){
        SharedPreferences.Editor editor = getSharedPreferences(cx).edit();
        editor.putString("USERNAME", username);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString("USERNAME", "");
    }

    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}

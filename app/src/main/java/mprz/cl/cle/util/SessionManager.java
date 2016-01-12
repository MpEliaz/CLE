package mprz.cl.cle.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import mprz.cl.cle.clases.Persona;

/**
 * Created by Elias Millachine on 19-10-2015.
 */
public class SessionManager {

    //Logcat name
    private static String TAG = SessionManager.class.getSimpleName();

    //Shared Prefereces
    SharedPreferences pref;
    Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    //Shared Preferences file name
    private static final String PREF_NAME = "CLE";

    public SessionManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void guardarUsuarioLogeado(Persona p){
        editor.putString("rut", p.getRut());
        editor.putString("nombre", p.getNombre());
        editor.putBoolean("logeado", true);
        editor.commit();

        Log.d(TAG, "Nuevas credenciales guardadas");
    }

    public Persona obtenerUsuarioLogeado(){
        String rut = pref.getString("rut", "");
        String nombre = pref.getString("nombre", "");

        Persona p = new Persona();
        p.setRut(rut);
        p.setNombre(nombre);
        Log.d(TAG, "usuario logeado obtenido");
        return p;
    }

    public void EliminarUsuarioLogeado(){
        editor.clear();
        editor.commit();

        Log.d(TAG, "usuario logeado eliminado");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean("logeado", false);
    }
}

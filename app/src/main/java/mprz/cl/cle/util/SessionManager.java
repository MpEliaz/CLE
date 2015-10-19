package mprz.cl.cle.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

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
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public SessionManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGED_IN,isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}

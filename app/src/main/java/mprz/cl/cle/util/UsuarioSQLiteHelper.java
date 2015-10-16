package mprz.cl.cle.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by elias on 14-10-15.
 */
public class UsuarioSQLiteHelper extends SQLiteOpenHelper {

    String UsuarioTable= "CREATE TABLE Usuario (id INTEGER AUTOINCREMENT, nombres TEXT, apellido_p TEXT, apellido_m TEXT, estado INTEGER)";

    public UsuarioSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(UsuarioTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        db.execSQL(UsuarioTable);
    }
}

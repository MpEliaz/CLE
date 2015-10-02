package mprz.cl.cle.conectivdad;

import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by elias on 28-09-15.
 */
public class ValidarUsuario extends AsyncTask<String, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {

        getDatos(params[0], params[1]);

        return null;
    }


    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

    public void getDatos(String usuario, String password) {


    }


}


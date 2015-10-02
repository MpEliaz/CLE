package mprz.cl.cle.clases;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by elias on 01-10-15.
 */
public final class CLESingleton {

    private static CLESingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;

    private CLESingleton(Context context){
        CLESingleton.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized CLESingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new CLESingleton(context);
        }
        return singleton;
    }

    private RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public  void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }



}

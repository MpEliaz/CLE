package mprz.cl.cle.clases;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import mprz.cl.cle.util.LruBitmapCache;

/**
 * Created by elias on 01-10-15.
 */
public final class CLESingleton {

    private static CLESingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;
    private ImageLoader mImageLoader;

    private CLESingleton(Context context){
        this.context = context;
        requestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(requestQueue, new LruBitmapCache(LruBitmapCache.getCacheSize(this.context)));





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

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}

package mprz.cl.cle.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

import mprz.cl.cle.R;
import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.clases.Noticia;

/**
 * Created by elias on 07-12-15.
 */
public class adaptadorNoticiasHome extends RecyclerView.Adapter<adaptadorNoticiasHome.NoticiaViewHolder> {

    private Context cx;
    private ArrayList<Noticia> noticias;

    public adaptadorNoticiasHome(Context cx, ArrayList<Noticia> noticias) {
        this.cx = cx;
        this.noticias = noticias;
    }

    @Override
    public NoticiaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(cx).inflate(R.layout.item_noticia_preview, parent, false);
        final NoticiaViewHolder vh = new NoticiaViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(NoticiaViewHolder holder, int position) {

        Noticia n = noticias.get(position);
        holder.bindNoticia(n);
    }

    @Override
    public int getItemCount() {
        if(noticias != null){
            return noticias.size();
        }
        return 0;
    }

    public class NoticiaViewHolder extends RecyclerView.ViewHolder {

        private int id;
        private TextView titulo;
        private TextView cuerpo;
        private ImageView imagen;

        public NoticiaViewHolder(View v) {
            super(v);

            titulo = (TextView)v.findViewById(R.id.titulo_noticia);
            cuerpo = (TextView)v.findViewById(R.id.resumen_noticia);
            imagen = (ImageView)v.findViewById(R.id.img_thumbnail);


        }

        public void bindNoticia(final Noticia n){
            titulo.setText(n.getTitulo());
            cuerpo.setText(n.getCuerpo());
            ImageRequest req = new ImageRequest("http://cle.ejercito.cl/upload/" + n.getUrl_imagen(),
                    new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    imagen.setImageBitmap(response);
                }
            }, 0, 0, null,
                    new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            CLESingleton.getInstance(cx).addToRequestQueue(req);




        }
    }

    public void updateData(ArrayList<Noticia> data) {
        noticias.clear();
        noticias.addAll(data);
        notifyDataSetChanged();
    }
}

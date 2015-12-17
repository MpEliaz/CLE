package mprz.cl.cle;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import mprz.cl.cle.R;
import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.clases.Noticia;
import mprz.cl.cle.util.SQLiteHandler;

public class NoticiaDetalle extends AppCompatActivity {

    private SQLiteHandler db;
    ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        int id = getIntent().getIntExtra("id_noticia",-1);
        db = new SQLiteHandler(this);
        Noticia n = db.obtenerNoticia(id);

        if(n != null){
            NetworkImageView imagen = (NetworkImageView)findViewById(R.id.noticia_detalle_imagen);
            TextView titulo = (TextView)findViewById(R.id.noticia_detalle_titulo);
            titulo.setText(n.getTitulo());
            TextView cuerpo = (TextView)findViewById(R.id.noticia_detalle_completo);
            cuerpo.setText(n.getCuerpo());

            mImageLoader = CLESingleton.getInstance(this).getImageLoader();
            imagen.setImageUrl("http://cle.ejercito.cl/upload/" + n.getUrl_imagen(), mImageLoader);
        }
    }

}

package mprz.cl.cle;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.drawee.view.SimpleDraweeView;

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

        inicializarToolbar();

        int id = getIntent().getIntExtra("id_noticia",-1);
        db = new SQLiteHandler(this);
        Noticia n = db.obtenerNoticia(id);

        if(n != null){

            SimpleDraweeView imagen = (SimpleDraweeView)findViewById(R.id.noticia_detalle_imagen);
            Uri uri = Uri.parse("http://cle.ejercito.cl/upload/" + n.getUrl_imagen());
            imagen.setImageURI(uri);

            TextView titulo = (TextView)findViewById(R.id.noticia_detalle_titulo);
            titulo.setText(n.getTitulo());
            TextView cuerpo = (TextView)findViewById(R.id.noticia_detalle_completo);
            cuerpo.setText(n.getCuerpo());

        }
    }

    private void inicializarToolbar() {

        //App bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_news);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}

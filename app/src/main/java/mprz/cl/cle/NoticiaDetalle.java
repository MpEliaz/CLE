package mprz.cl.cle;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.drawee.view.SimpleDraweeView;

import mprz.cl.cle.clases.Noticia;
import mprz.cl.cle.util.SQLiteHandler;

public class NoticiaDetalle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia_detalle);

        inicializarToolbar();

        int id = getIntent().getIntExtra("id_noticia",-1);
        SQLiteHandler db = new SQLiteHandler(this);
        Noticia n = db.obtenerNoticia(id);

        if(n != null){

            SimpleDraweeView imagen = (SimpleDraweeView)findViewById(R.id.noticia_detalle_imagen);
            Uri uri = Uri.parse("http://cle.ejercito.cl/upload/" + n.getUrl_imagen());
            imagen.setImageURI(uri);

            TextView titulo = (TextView)findViewById(R.id.noticia_detalle_titulo);
            titulo.setText(n.getTitulo());
            TextView cuerpo = (TextView)findViewById(R.id.noticia_detalle_completo);
            //WebView cuerpo = (WebView) findViewById(R.id.noticia_detalle_completo2);
            cuerpo.setText(n.getCuerpo());
/*            WebSettings settings = cuerpo.getSettings();
            settings.setDefaultTextEncodingName("utf-8");
            String text = "<html><meta charset=\"UTF-8\"><body style=\"background-color:#FFDFDDDD\"><p align=\"justify\">"+n.getCuerpo()+"</p></body></html>";
            cuerpo.loadData(text, "text/html; charset=utf-8", "utf-8");*/


        }
    }

    private void inicializarToolbar() {

        //App bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_news);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

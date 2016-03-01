package mprz.cl.cle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import mprz.cl.cle.R;
import mprz.cl.cle.adaptadores.adaptadorPagerDocumentos;
import mprz.cl.cle.clases.Documento;
import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.fragments.paginaDocumento;

public class DocumentosPager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentos_pager);
        Bundle extras = getIntent().getExtras();
        ViewPager pager = (ViewPager)findViewById(R.id.documentos_viewer);
        pager.setOffscreenPageLimit(3);

        int paginas[] = extras.getIntArray("ids");
        adaptadorPagerDocumentos adapter = new adaptadorPagerDocumentos(paginas);

        pager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_documentos_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Fragment> getFragments(String[] nombres) {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        for (int i = 0; i < nombres.length; i++) {
            items.add(paginaDocumento.newInstance(nombres[i]));
        }

        return items;
    }
}

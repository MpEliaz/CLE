package mprz.cl.cle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mprz.cl.cle.adaptadores.adaptadorEncuesta;
import mprz.cl.cle.adaptadores.adaptadorEncuestaPager;
import mprz.cl.cle.adaptadores.adaptadorEncuestados;
import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.Respuesta;
import mprz.cl.cle.util.SQLiteHandler;

public class showEncuesta extends AppCompatActivity {

    private TextView test;
    private RecyclerView rv_encuesta;
    private ViewPager pager;
    private adaptadorEncuestaPager adapter;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_encuesta);

        inicializarToolbar();

        db = new SQLiteHandler(this);

        Bundle extras = getIntent().getExtras();

        pager = (ViewPager) findViewById(R.id.EncuestaPager);

        /*rv_encuesta = (RecyclerView)findViewById(R.id.rv_encuesta);
        rv_encuesta.setHasFixedSize(true);
        rv_encuesta.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));*/

        ArrayList<Pregunta> list = db.ObtenerEncuestaFromDB();


        ArrayList<PreguntaEncuesta> fragments = getFragments(list);

        adapter = new adaptadorEncuestaPager(getSupportFragmentManager(), fragments);
        pager.setAdapter(adapter);



    }

    private void inicializarToolbar() {


        //App bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_encuesta);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Encuesta");

/*        final ActionBar ab = getSupportActionBar();

        if(ab != null){
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }*/

    }

    private ArrayList<PreguntaEncuesta> getFragments(ArrayList<Pregunta> list) {
        ArrayList<PreguntaEncuesta> items = new ArrayList<PreguntaEncuesta>();

        for (Pregunta p : list) {

            items.add(PreguntaEncuesta.newIntance(p));

        }

        return items;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_encuestas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.next_question) {

            avancePagina();
        }

        return super.onOptionsItemSelected(item);
    }

    private void avancePagina(){

        Fragment f = adapter.getItem(pager.getCurrentItem());
        RadioGroup rg = (RadioGroup)f.getView().findViewById(R.id.rg_preg);
        int checked = rg.getCheckedRadioButtonId();

        if(checked == -1){
            Toast.makeText(showEncuesta.this, "Seleccione opción", Toast.LENGTH_SHORT).show();
        }
        else{
            pager.setCurrentItem(pager.getCurrentItem()+1);
        }

    }
}

package mprz.cl.cle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import mprz.cl.cle.adaptadores.adaptadorEncuesta;
import mprz.cl.cle.adaptadores.adaptadorEncuestaPager;
import mprz.cl.cle.adaptadores.adaptadorEncuestados;
import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.Respuesta;

public class showEncuesta extends AppCompatActivity {

    private TextView test;
    private RecyclerView rv_encuesta;
    private ViewPager pager;
    private adaptadorEncuestaPager adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_encuesta);

        Bundle extras = getIntent().getExtras();

        pager = (ViewPager) findViewById(R.id.EncuestaPager);

        /*rv_encuesta = (RecyclerView)findViewById(R.id.rv_encuesta);
        rv_encuesta.setHasFixedSize(true);
        rv_encuesta.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));*/

        ArrayList<Pregunta> list = new ArrayList<Pregunta>();

        for (int j = 0; j < 10; j++) {

            Pregunta p = new Pregunta();

            p.setId(j);
            p.setTitulo("¿Que opina sobre el comportamiento de su superior?");


            ArrayList<Respuesta> respuestas = new ArrayList<Respuesta>();

            for (int i = 0; i < 5; i++) {

                Respuesta r = new Respuesta();
                r.setId(i);
                r.setRespuesta("Respuesta "+i);
                respuestas.add(r);

            }

            p.setRespuestas(respuestas);
            list.add(p);

        }

        ArrayList<PreguntaEncuesta> fragments = getFragments(list);

        adapter = new adaptadorEncuestaPager(getSupportFragmentManager(), fragments);
        pager.setAdapter(adapter);



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
        getMenuInflater().inflate(R.menu.menu_encuesta, menu);
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
}

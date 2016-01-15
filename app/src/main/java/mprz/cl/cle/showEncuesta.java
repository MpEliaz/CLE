package mprz.cl.cle;

import android.app.ProgressDialog;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mprz.cl.cle.adaptadores.adaptadorEncuesta;
import mprz.cl.cle.adaptadores.adaptadorEncuestaPager;
import mprz.cl.cle.adaptadores.adaptadorEncuestados;
import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Persona;
import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.Respuesta;
import mprz.cl.cle.fragments.MisEncuestas;
import mprz.cl.cle.util.SQLiteEncuestasHandler;
import mprz.cl.cle.util.SQLiteHandler;
import mprz.cl.cle.util.SessionManager;

import static mprz.cl.cle.util.Constantes.URL;

public class showEncuesta extends AppCompatActivity {

    private String url_encuesta = URL + "/encuestaJson2?AspxAutoDetectCookieSupport=1";
    private TextView test;
    private RecyclerView rv_encuesta;
    private ViewPager pager;
    private adaptadorEncuestaPager adapter;
    private SQLiteEncuestasHandler db_encuestas;
    private Menu menu;
    private ProgressDialog pDialog;
    private String run_evaluado;
    private int id_encuesta_;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_encuesta);

        inicializarToolbar();

        db_encuestas = new SQLiteEncuestasHandler(this);
        session = new SessionManager(this);
        Persona p = session.obtenerUsuarioLogeado();
        Bundle extras = getIntent().getExtras();
        run_evaluado = extras.getString("runEvaluado");
        id_encuesta_ = extras.getInt("id_encuesta");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pager = (ViewPager) findViewById(R.id.EncuestaPager);


        ArrayList<Pregunta> list = db_encuestas.ObtenerEncuestaFromDB(id_encuesta_);
        ArrayList<Fragment> fragments = new ArrayList<>();

        if(list.size() > 0){
            fragments = getFragments(list);
        }
        else{
            obtenerEncuesta(p.getRut(), run_evaluado);
        }


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

    private ArrayList<Fragment> getFragments(ArrayList<Pregunta> list) {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        for (Pregunta p : list) {

            items.add(PreguntaEncuesta.newIntance(p));

        }
        FragmentFinalEncuesta f = new FragmentFinalEncuesta();
        items.add(f);

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
            int total = adapter.getCount();
            int actual = pager.getCurrentItem()+1;

            if(actual < total){

                pager.setCurrentItem(pager.getCurrentItem()+1);
            }
        }
    }

    private void obtenerEncuesta(final String evaluador, final String evaluado) {

        pDialog.setMessage("Obteniendo encuesta...");
        showDialog();

        StringRequest req = new StringRequest(Request.Method.POST, url_encuesta, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int id_encuesta;

                try {
                    JSONObject o = new JSONObject(response);
                    id_encuesta = Integer.parseInt(o.getString("id"));

                    JSONArray preguntas = o.getJSONArray("preguntas");
                    ArrayList<Pregunta> set_Preguntas = new ArrayList<Pregunta>();

                    for (int i = 0; i < preguntas.length(); i++) {

                        JSONObject pregunta = preguntas.getJSONObject(i);
                        Pregunta p = new Pregunta();
                        p.setId_texto(pregunta.getString("id"));
                        p.setTitulo(pregunta.getString("pregunta"));

                        JSONArray respuestas = pregunta.getJSONArray("respuestas");
                        ArrayList<Respuesta> setRespuestas = new ArrayList<Respuesta>();

                        for (int j = 0; j < respuestas.length(); j++) {
                            JSONObject resp = respuestas.getJSONObject(j);
                            Respuesta r = new Respuesta();
                            r.setId(Integer.parseInt(resp.getString("id")));
                            r.setRespuesta(resp.getString("respuesta"));
                            setRespuestas.add(r);
                        }

                        p.setRespuestas(setRespuestas);
                        set_Preguntas.add(p);
                    }

                    db_encuestas.guardarEncuesta(set_Preguntas, id_encuesta);

                    ArrayList<Pregunta> list = db_encuestas.ObtenerEncuestaFromDB(id_encuesta);
                    ArrayList<Fragment> fragments = getFragments(list);
                    adapter.updateData(fragments);
                    hideDialog();

                }catch (JSONException e)
                {
                    e.printStackTrace();
                    //Toast.makeText(this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                params.put("run_evaluador", evaluador);
                params.put("run_evaluado", evaluado);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };
        CLESingleton.getInstance(this).addToRequestQueue(req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

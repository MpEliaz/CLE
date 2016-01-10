package mprz.cl.cle;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import mprz.cl.cle.R;
import mprz.cl.cle.adaptadores.adaptadorBuscaEvaluadores;
import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Persona;
import mprz.cl.cle.util.EvaluadoresDialog;
import mprz.cl.cle.util.SQLiteEncuestasHandler;
import mprz.cl.cle.util.SQLiteHandler;

import static mprz.cl.cle.util.Constantes.URL;

public class buscadorEvaluadores extends AppCompatActivity implements adaptadorBuscaEvaluadores.OnItemClickListener {

    private String url = URL + "/ObtenerNombres?AspxAutoDetectCookieSupport=1";
    private String url_test = "http://192.168.50.19:8000/buscar_evaluadores";
    private ArrayList<Persona> data;
    private SQLiteHandler db;
    adaptadorBuscaEvaluadores adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador_evaluadores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBuscador);
        setSupportActionBar(toolbar);
        db = new SQLiteHandler(this);


        data = new ArrayList<>();
        RecyclerView rv = (RecyclerView)findViewById(R.id.result_evaluadores);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new adaptadorBuscaEvaluadores(this,data);
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_busqueda_evaluadores, menu);

        MenuItem searchItem = menu.findItem(R.id.search_evaluadores);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;

        if (searchItem != null){
            searchView = (SearchView)searchItem.getActionView();
        }

        if (searchView != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint("Buscar evaluadores");
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String q) {
                    Log.i("search submit",q);
                    if(!q.equals("") && q.length()>2){

                        buscarEvaluadores(q);
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String q) {
                   // Log.i("search",q);

                    return true;
                }
            };

            searchView.setOnQueryTextListener(queryTextListener);
        }

        return true;
    }

    private void buscarEvaluadores(final String q){

        StringRequest request = new StringRequest(Request.Method.GET, url_test, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("respuesta", response);

                data = new ArrayList<Persona>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);

                        Persona p = new Persona();
                        p.setRut(o.getString("id"));
                        p.setNombre(o.getString("text"));
                        data.add(p);
                    }

                    if(data.size()>0){
                    adapter.updateData(data);
                    }
                    else{
                        Toast.makeText(buscadorEvaluadores.this, "No hay resultados.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("respuesta", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("q", q);
                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        CLESingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onItemClick(View view, Persona persona, int position) {

        Bundle args = new Bundle();
        args.putString("rut", persona.getRut());
        args.putString("nombre", persona.getNombre());
        EvaluadoresDialog newFragment = new EvaluadoresDialog();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "dialog_evaluadores");
    }

    public void doPositiveClick(String rut, String nombre, int relacion) {

        boolean  pase = false;
        if(db.obtenerEvaluador(rut) == null){

            validarListaDeEvaluadores(rut, nombre, relacion);

        }
        else {
            Toast.makeText(this, "Evaluador seleccionado ya existe.", Toast.LENGTH_SHORT).show();
        }
    }

    public void doNegativeClick() {

    }

    private void validarListaDeEvaluadores(String rut, String nombre, int relacion){

        boolean pase = false;
        int superiores = 0;
        int pares = 0;
        int subalternos = 0;

        ArrayList<Persona> data = db.obtenerMisEvaluadores();

        for (Persona p: data) {

            switch (p.getCategoria()){
                case "1":
                    superiores++;
                    break;
                case "2":
                    pares++;
                    break;
                case "3":
                    subalternos++;
                    break;
            }
        }

        if(relacion == 1){
            if(superiores < 1){
                pase = true;
            }
            else{
                Toast.makeText(this, "Ya has agregado un Superior a tu lista", Toast.LENGTH_LONG).show();
            }
        }


        if(relacion == 2){
            if(pares < 5){
                pase = true;
            }
            else {
                Toast.makeText(this, "Ya has llegado al limite de pares agregados a tu lista", Toast.LENGTH_LONG).show();
            }
        }

        if(relacion == 3){
            if(subalternos < 5){
                pase = true;
            }
            else {
                Toast.makeText(this, "Ya has llegado al limite de subalternos agregados a tu lista", Toast.LENGTH_LONG).show();
            }
        }

        if(pase){
            db.guardarEvaluador(rut, nombre, relacion);
            finish();
        }
    }

}

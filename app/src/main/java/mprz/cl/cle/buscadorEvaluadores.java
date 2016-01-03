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
import mprz.cl.cle.adaptadores.adaptadorEvaluadores;
import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Persona;
import mprz.cl.cle.util.EvaluadoresDialog;
import mprz.cl.cle.util.SQLiteEncuestasHandler;
import mprz.cl.cle.util.SQLiteHandler;

import static mprz.cl.cle.util.Constantes.URL;

public class buscadorEvaluadores extends AppCompatActivity implements adaptadorEvaluadores.OnItemClickListener {

    private String url = URL + "/ObtenerNombres?AspxAutoDetectCookieSupport=1";
    private ArrayList<Persona> data;
    private SQLiteHandler db;
    adaptadorEvaluadores adapter;


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

        adapter = new adaptadorEvaluadores(this,data);
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mis_evaluadores, menu);

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
                    Log.i("search",q);

                    return true;
                }
            };

            searchView.setOnQueryTextListener(queryTextListener);
        }

        return true;
    }

    private void buscarEvaluadores(final String q){

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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

    public void doPositiveClick(String rut, String nombre) {

        if(db.obtenerEvaluador(rut) == null){
            db.guardarEvaluador(rut,nombre);
            finish();
        }
        else {
            Toast.makeText(this, "Evaluador seleccionado ya existe.", Toast.LENGTH_SHORT).show();
        }
    }

    public void doNegativeClick() {

    }

}

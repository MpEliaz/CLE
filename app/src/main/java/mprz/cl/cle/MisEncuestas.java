package mprz.cl.cle;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import mprz.cl.cle.adaptadores.adaptadorEncuestados;
import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Persona;
import mprz.cl.cle.util.SQLiteHandler;

import static mprz.cl.cle.util.Constantes.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class MisEncuestas extends Fragment implements adaptadorEncuestados.OnItemClickListener{

    private RecyclerView rv;
    private String url = URL + "/ObtenerEvaluacionesJson?AspxAutoDetectCookieSupport=1";
    private ArrayList<Encuesta> data;
    private adaptadorEncuestados adapter;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mis_encuestas, container, false);

        db = new SQLiteHandler(getActivity());

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        rv = (RecyclerView) v.findViewById(R.id.rv_encuestas);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new adaptadorEncuestados(db.ObtenerEncuestados(), getActivity());
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);

        return v;
    }

    private void getData(String rut) {

        pDialog.setMessage("Obteniendo datos...");
        showDialog();

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.i("CLE", "encuestas OK");
                data = new ArrayList<Encuesta>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);

                        Encuesta e = new Encuesta();
                        e.setRunEvaluador(o.getString("runEvaluador"));
                        e.setRunEvaluado(o.getString("runEvaluado"));
                        e.setNombreEvaluado(o.getString("nombreEvaluado"));
                        e.setRelacion(o.getString("relacion"));
                        e.setEstado(o.getString("estado"));
                        data.add(e);
                    }
                    hideDialog();
                    db.eliminarEncuestados();
                    db.guardarEncuestados(data);
                    data = db.ObtenerEncuestados();
                    adapter.updateData(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("CLE", "encuestas Error: " + error.getMessage());
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("runEvaluador", "15650686-9");
                params.put("periodo", "2015");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };
        CLESingleton.getInstance(getActivity()).addToRequestQueue(req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onItemClick(View view, Encuesta encuesta, int position) {

        Toast.makeText(getActivity(), "nombre: " + encuesta.getNombreEvaluado(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_mis_encuestas, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.update_encuestas:
                getData("17939855-9");
               // Toast.makeText(getActivity(), "Actualizado", Toast.LENGTH_LONG).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


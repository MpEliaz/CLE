package mprz.cl.cle.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import mprz.cl.cle.adaptadores.adaptadorEvaluadores;
import mprz.cl.cle.buscadorEvaluadores;
import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.clases.Persona;
import mprz.cl.cle.util.SQLiteHandler;

import static mprz.cl.cle.util.Constantes.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class misEvaluadores extends Fragment implements adaptadorEvaluadores.OnItemLongClickListener{

    private String url = URL + "/ObtenerNombres?AspxAutoDetectCookieSupport=1";
    private int EVALUADORES = 777;
    private adaptadorEvaluadores adapter;
    private ArrayList<Persona> data;
    private SQLiteHandler db;
    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        db = new SQLiteHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mis_evaluadores, container, false);

        data = new ArrayList<>();
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.rv_mis_evaluadores);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        buscarEvaluadoresWS("17288811");
        adapter = new adaptadorEvaluadores(getActivity(), data);
        adapter.setOnLongClickListener(this);
        rv.setAdapter(adapter);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), buscadorEvaluadores.class);
                startActivityForResult(i, EVALUADORES);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EVALUADORES){
            adapter.updateData(db.obtenerMisEvaluadores());

        }

    }

    private void buscarEvaluadoresWS(final String rut){

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
                        fab.setVisibility(View.GONE);
                    }
                    else{
                        adapter.updateData(db.obtenerMisEvaluadores());
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
                params.put("q", "kjhkj");
                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        CLESingleton.getInstance(getActivity()).addToRequestQueue(request);
    }


    @Override
    public void onItemLongClick(View view, final Persona persona, final int position) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Confirmación");
        alertDialog.setMessage("¿Desea eliminar de su lista de evaluadores?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.removeItem(position);
                        db.eliminarEvaluador(persona.getRut());
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        alertDialog.show();
    }
}

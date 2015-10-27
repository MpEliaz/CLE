package mprz.cl.cle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import mprz.cl.cle.adaptadores.adaptadorEncuestados;
import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.clases.Persona;

import static mprz.cl.cle.util.Constantes.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class MisEncuestas extends Fragment {

    private ArrayList<Persona> datos;
    private RecyclerView rv;
    private String url = URL + "/ObtenerEvaluacionesJson?AspxAutoDetectCookieSupport=1";
    private ArrayList<Persona> data;
    private adaptadorEncuestados adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

 //       datos = getData("17939855-9");

/*        for (int i = 0; i < 10; i++) {
            datos.add(new Persona(i,"17.288.811-9"+1,"ELIAS MILLACHINE "+1, "Superior "+i));
        }*/

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private ArrayList<Persona> getData(String rut) {


        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                data = new ArrayList<Persona>();

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);

                        Persona p = new Persona();
                        p.setRut(o.getString("runEvaluado"));
                        p.setNombre(o.getString("nombreEvaluado"));
                        p.setCategoria(o.getString("relacion"));
                        data.add(p);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CLE", "encuestas Error: " + error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                params.put("runEvaluador", "17939855-9");
                params.put("periodo", "2015");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

        };
        CLESingleton.getInstance(getActivity()).addToRequestQueue(req);
        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mis_encuestas, container, false);

        rv = (RecyclerView)v.findViewById(R.id.rv_encuestas);
        rv.setHasFixedSize(true);

        final adaptadorEncuestados adapter = new adaptadorEncuestados(getData("17939855-9"));

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DemoRecView", "Pulsado el elemento " + rv.getChildPosition(view));
            }
        });

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return v;
    }


}


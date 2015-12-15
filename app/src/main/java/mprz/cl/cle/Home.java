package mprz.cl.cle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mprz.cl.cle.R;
import mprz.cl.cle.adaptadores.adaptadorNoticiasHome;
import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Noticia;
import mprz.cl.cle.util.SQLiteHandler;

import static mprz.cl.cle.util.Constantes.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    private RecyclerView rv;
    private adaptadorNoticiasHome adapter;
    private ArrayList<Noticia> noticias;
    private String url = URL + "/noticiasJson?AspxAutoDetectCookieSupport=1";
    private SQLiteHandler db;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        db = new SQLiteHandler(getActivity());
        mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.refresh_noticias);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                obtenerData();
            }
        });


        Noticia a = new Noticia();
        noticias = db.obtenerNoticias();
        adapter = new adaptadorNoticiasHome(getActivity(), noticias);
        rv = (RecyclerView) v.findViewById(R.id.noticias_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);

        return v;
    }

    public void obtenerData() {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                noticias = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);

                        Noticia n = new Noticia();
                        n.setId(o.getInt("id"));
                        n.setUsuario(o.getString("usuario"));
                        n.setTitulo(o.getString("Titulo"));
                        n.setResumen(o.getString("Resumen"));
                        n.setCuerpo(o.getString("Completa"));
                        n.setUrl_imagen(o.getString("Imagen"));

                        noticias.add(n);
                    }

                    db.eliminarNoticias();
                    db.guardarNoticias(noticias);
                    adapter.updateData(db.obtenerNoticias());
                    adapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {

                }

            }

        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        CLESingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
}


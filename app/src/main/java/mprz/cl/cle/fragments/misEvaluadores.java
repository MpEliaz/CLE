package mprz.cl.cle.fragments;


import android.app.ProgressDialog;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mprz.cl.cle.R;
import mprz.cl.cle.adaptadores.adaptadorEvaluadores;
import mprz.cl.cle.buscadorEvaluadores;
import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.clases.Persona;
import mprz.cl.cle.util.SQLiteHandler;
import mprz.cl.cle.util.SessionManager;

import static mprz.cl.cle.util.Constantes.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class misEvaluadores extends Fragment implements adaptadorEvaluadores.OnItemClickListener{

    private String url = URL + "/listaEvaluadores?AspxAutoDetectCookieSupport=1";
    private String url_envio = URL + "/guardarEvaluadores?AspxAutoDetectCookieSupport=1";
    private String url_envio_test = "http://192.168.50.19:8000/recibir_evaluadores";
    private int EVALUADORES = 777;
    private adaptadorEvaluadores adapter;
    private ArrayList<Persona> data;
    private SQLiteHandler db;
    FloatingActionButton fab;
    private View.OnClickListener add_evaluadores;
    private View.OnClickListener actualizar_evaluadores;
    private ProgressDialog pDialog;
    private ProgressDialog pDialog2;
    private SessionManager session;

    Menu menu;
    MenuItem menuDoneItem;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        db = new SQLiteHandler(getActivity());
        session = new SessionManager(getContext());


        add_evaluadores = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), buscadorEvaluadores.class);
                startActivityForResult(i, EVALUADORES);

            }
        };
        actualizar_evaluadores = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviarEvaluadores();

            }
        };

        data = new ArrayList<>();
        adapter = new adaptadorEvaluadores(getActivity(), data, 1);
        adapter.setOnItemClickListener(this);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Obteniendo datos desde el servidor. Por favor espere...");
        pDialog.setCancelable(false);

        pDialog2 = new ProgressDialog(getContext());
        pDialog2.setMessage("Subiendo datos al servidor. Por favor espere...");
        pDialog2.setCancelable(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mis_evaluadores, container, false);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv_mis_evaluadores);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);
        pDialog.show();
        Persona p = session.obtenerUsuarioLogeado();
        buscarEvaluadoresWS(p.getRut());
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_mis_evaluadores, menu);

        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_evaluadores:
                //Toast.makeText(getActivity(), "soy el boton actualizar", Toast.LENGTH_SHORT).show();
                enviarEvaluadores();
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EVALUADORES){
            ArrayList<Persona> evaluadores = db.obtenerMisEvaluadores();
            adapter.updateData(evaluadores, 1);

            if(verificarSubidaLista(evaluadores)){

                if(menu!= null){
                    menuDoneItem = menu.findItem(R.id.update_evaluadores);
                    menuDoneItem.setVisible(true);
                    menuDoneItem.setEnabled(true);
                }
            }else {
                if(menu!= null){
                    menuDoneItem = menu.findItem(R.id.update_evaluadores);
                    menuDoneItem.setVisible(false);
                    menuDoneItem.setEnabled(false);
                }
            }

            if (evaluadores.size() < 11 && evaluadores.size() >= 0){
                fab.setImageResource(android.R.drawable.ic_input_add);
                fab.setOnClickListener(add_evaluadores);
                fab.setVisibility(View.VISIBLE);
            }
            else{
                fab.setVisibility(View.GONE);
            }

        }

    }

    private void buscarEvaluadoresWS(final String rut){

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("respuesta OK", response);
                Log.i("rut: ", rut);

                ArrayList<Persona> datos = new ArrayList<Persona>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);

                        Persona p = new Persona();
                        p.setRut(o.getString("run_evaluador"));
                        p.setNombre(o.getString("nombre_evaluador"));
                        p.setEstado_encuesta(o.getString("estado"));
                        p.setCategoria(o.getString("relacion"));
                        datos.add(p);
                    }

                    if(datos.size()>0){
                        adapter.updateData(datos,2);

                    }
                    else{
                        datos = db.obtenerMisEvaluadores();
                        adapter.updateData(datos,1);
                        if(verificarSubidaLista(datos)){
                            if(menu!= null){
                                menuDoneItem = menu.findItem(R.id.update_evaluadores);
                                menuDoneItem.setVisible(true);
                                menuDoneItem.setEnabled(true);
                            }

                        }
                        else {
                            if(menu!= null){
                                menuDoneItem = menu.findItem(R.id.update_evaluadores);
                                menuDoneItem.setVisible(false);
                                menuDoneItem.setEnabled(false);
                            }
                        }

                        if (datos.size() < 11 && datos.size() >= 0){
                            fab.setImageResource(android.R.drawable.ic_input_add);
                            fab.setOnClickListener(add_evaluadores);
                            fab.setVisibility(View.VISIBLE);
                        }
                        else{
                            fab.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("error conexion", error.getMessage());
                pDialog.hide();
                Toast.makeText(getActivity(), "Imposible conectar con el servidor, intente mas tarde.", Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("run", rut);
                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        CLESingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void enviarEvaluadores() {

        pDialog2.show();
        final ArrayList<Persona> lista = db.obtenerMisEvaluadores();
        if(verificarSubidaLista(lista))
        {
            StringRequest req = new StringRequest(Request.Method.POST, url_envio_test, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("respuesta", response);
                    pDialog2.hide();
                    //TODO: manejar la respuesta que entregue el servidor
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("respuesta", error.getMessage());
                    pDialog2.hide();

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    String runEvaluado ="";
                    params.put("par1", "");
                    params.put("par2", "");
                    params.put("par3", "");
                    params.put("par4", "");
                    params.put("par5", "");
                    params.put("sub1", "");
                    params.put("sub2", "");
                    params.put("sub3", "");
                    params.put("sub4", "");
                    params.put("sub5", "");



                    int par=1;
                    int sub=1;

                    params.put("run_evaluado", runEvaluado);
                    for (Persona p : lista) {
                        if(p.getCategoria().equals("1")){
                            params.put("sup", p.getRut());

                        }else if(p.getCategoria().equals("2")){
                            params.put("par"+par, p.getRut());
                            par++;
                        }else if(p.getCategoria().equals("3")){
                            params.put("sub"+sub, p.getRut());
                            sub++;
                        }

                    }

                    return params;
                }
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }
            };
            CLESingleton.getInstance(getActivity()).addToRequestQueue(req);
        }
    }

    @Override
    public void onItemClick(View view, final Persona persona, final int position) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Confirmación");
        alertDialog.setMessage("¿Desea eliminar de su lista de evaluadores?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.removeItem(position);
                        db.eliminarEvaluador(persona.getRut());

                        if(verificarSubidaLista(db.obtenerMisEvaluadores())){
                            if(menu!= null){
                                menuDoneItem = menu.findItem(R.id.update_evaluadores);
                                menuDoneItem.setVisible(true);
                                menuDoneItem.setEnabled(true);
                            }
                        }
                        else {
                            menuDoneItem = menu.findItem(R.id.update_evaluadores);
                            menuDoneItem.setVisible(false);
                            menuDoneItem.setEnabled(false);
                        }
                        if (adapter.getItemCount() < 11 && adapter.getItemCount() >= 0){
                            fab.setImageResource(android.R.drawable.ic_input_add);
                            fab.setOnClickListener(add_evaluadores);
                            fab.setVisibility(View.VISIBLE);
                        }
                        else{
                            fab.setVisibility(View.GONE);
                        }
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        alertDialog.show();
    }

    private boolean verificarSubidaLista(ArrayList<Persona> data){

        boolean pase = false;
        int superiores = 0;
        int pares = 0;
        int subalternos = 0;

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

        if(superiores == 1){
            if(pares >= 3 && pares <= 5){
                if(subalternos >=3 && subalternos <=5)
                {
                    pase = true;
                }
            }
        }

        return pase;
    }

}



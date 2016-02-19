package mprz.cl.cle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.util.SQLiteEncuestasHandler;
import mprz.cl.cle.util.SessionManager;

import static mprz.cl.cle.util.Constantes.URL;


public class FragmentFinalEncuesta extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RUN_EVALUADO = "run_evaluado";

    private String run_evaluado;
    private Button btn_finalizar;
    private SQLiteEncuestasHandler db_encuestas;
    private String url = URL + "/GuardarPreguntas?AspxAutoDetectCookieSupport=1";


    //private OnFragmentInteractionListener mListener;


    public static FragmentFinalEncuesta newInstance(String run_Evaluado) {
        FragmentFinalEncuesta fragment = new FragmentFinalEncuesta();
        Bundle args = new Bundle();
        args.putString(RUN_EVALUADO, run_Evaluado);

        fragment.setArguments(args);
        return fragment;
    }

    public FragmentFinalEncuesta() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            run_evaluado = getArguments().getString(RUN_EVALUADO);
        }

        db_encuestas = new SQLiteEncuestasHandler(getContext());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_finalizar_encuesta, container, false);
        btn_finalizar = (Button)v.findViewById(R.id.finalizar);

        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SessionManager s = new SessionManager(getContext());
                final JSONObject encuesta = db_encuestas.ObtenerEncuestaResuelta(run_evaluado, s.obtenerRutUsuarioLogeado());
                Log.i("CLE", encuesta.toString());

                //TODO falta agregar la url de envio al servidor y manejar respuesta del servidor.
                StringRequest req = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("exito", response);
                        try {
                            JSONObject o = new JSONObject(response);
                            if(o.getString("respuesta").equals("Exito")){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", error.getMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("sJson", encuesta.toString());
                        return params;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                };
                CLESingleton.getInstance(getActivity()).addToRequestQueue(req);


            }
        });

        return v;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        //Hides MenuItem action_edit
        MenuItem menuItem = menu.findItem(R.id.next_question);
        menuItem.setVisible(false);
    }
}

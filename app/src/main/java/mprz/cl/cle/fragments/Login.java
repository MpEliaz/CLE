package mprz.cl.cle.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.Respuesta;
import mprz.cl.cle.util.SQLiteEncuestasHandler;
import mprz.cl.cle.util.SessionManager;
import mprz.cl.cle.util.SQLiteHandler;

import static mprz.cl.cle.util.Constantes.URL;

public class Login extends Fragment {

    private EditText et_user;
    private EditText et_pass;
    private String url = URL + "/LoginJson?AspxAutoDetectCookieSupport=1";
    private String url_encuesta = URL + "/encuestaJson?AspxAutoDetectCookieSupport=1";
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteEncuestasHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_login,container, false);

        Button btn_login = (Button) v.findViewById(R.id.btn_login);
        et_user = (EditText) v.findViewById(R.id.et_user);
        et_pass = (EditText) v.findViewById(R.id.et_password);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteEncuestasHandler(getActivity());

        //SessionManager
        session = new SessionManager(getActivity());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
           /* Intent intent = new Intent(Login.this, MisEncuestas.class);
            startActivity(intent);
            finish();*/
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usuario = et_user.getText().toString().trim();
                String password = et_pass.getText().toString().trim();

                // Check for empty data in the form
                if (!usuario.isEmpty() && !password.isEmpty()) {
                    // login user
                    loginServidor(usuario, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getActivity(),
                            "Ingrese credenciales", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_mis_encuestas, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    private void loginServidor(final String user, final String pass){

        pDialog.setMessage("Iniciando sesion...");
        showDialog();

        StringRequest request = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("CLE", response);
                hideDialog();

                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject o = array.getJSONObject(0);
                    Log.e("CLE", o.getString("nombres"));

                    String error = o.getString("resp");
                    if(error.equals("OK")){

                        session.setLogin(true);

                       db.guardarUsuario(o.getString("nombres"), o.getString("paterno"), o.getString("materno"));

                        obtenerEncuesta();
                    }
                    else{
                        Toast.makeText(getActivity(),"Error en las credenciales",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CLE", "Login Error: " + error.getMessage());
                Toast.makeText(getActivity(),"Hubo un problema al intentar iniciar sesion. Intente mas tarde", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                    params.put("pass", pass);
                    params.put("run", user);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        CLESingleton.getInstance(getActivity()).addToRequestQueue(request);
        }

    private void obtenerEncuesta() {

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
                        p.setId(Integer.parseInt(pregunta.getString("id")));
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

                    db.guardarEncuesta(set_Preguntas, id_encuesta);

                    // Launch main activity
                    /*Intent i = new Intent(Login.this, MisEncuestas.class);
                    startActivity(i);*/
                    MisEncuestas nextFrag= new MisEncuestas();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_fragment, nextFrag)
                            .commit();

                }catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                params.put("id", "1");
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

}

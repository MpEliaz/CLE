package mprz.cl.cle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.Respuesta;
import mprz.cl.cle.clases.Usuario;
import mprz.cl.cle.util.SessionManager;
import mprz.cl.cle.util.SQLiteHandler;

import static mprz.cl.cle.util.Constantes.URL;

public class Login extends AppCompatActivity {

    private Button btn_login;
    private EditText et_user;
    private EditText et_pass;
    private String url = URL + "/LoginJson?AspxAutoDetectCookieSupport=1";
    private String url_encuesta = URL + "/encuestaJson?AspxAutoDetectCookieSupport=1";
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);

        btn_login = (Button) findViewById(R.id.btn_login);
        et_user = (EditText) findViewById(R.id.et_user);
        et_pass = (EditText) findViewById(R.id.et_password);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        //SessionManager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Login.this, MisEncuestas.class);
            startActivity(intent);
            finish();
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
                        Toast.makeText(getApplicationContext(),
                                "Ingrese credenciales", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actividad_principal, menu);
        return true;
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
                        Toast.makeText(Login.this,"Error en las credenciales",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CLE", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
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
        CLESingleton.getInstance(this).addToRequestQueue(request);
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
                    finish();

                }catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

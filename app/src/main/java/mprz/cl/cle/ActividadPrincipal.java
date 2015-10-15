package mprz.cl.cle;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.util.UsuarioSQLiteHelper;

import static mprz.cl.cle.util.Constantes.URL;

public class ActividadPrincipal extends AppCompatActivity {

    Button btn_login;
    EditText et_user;
    EditText et_pass;
    String url = URL + "/LoginJson?AspxAutoDetectCookieSupport=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);

        btn_login = (Button) findViewById(R.id.btn_login);
        et_user = (EditText) findViewById(R.id.et_user);
        et_pass = (EditText) findViewById(R.id.et_password);


        

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validarUsuario(et_user.getText().toString(), et_pass.getText().toString());
/*                if(et_user.getText().toString().equals("11111111-1") && et_pass.getText().toString().equals("juanperez")){

                    Intent i = new Intent(ActividadPrincipal.this, Inicio.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(ActividadPrincipal.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }*/
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

    private void validarUsuario(final String run, final String pass){

        StringRequest request = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("CLE", response.toString());

                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject o = array.getJSONObject(0);
                    Log.e("CLE", o.getString("nombres"));
                    if(!o.getString("resp").equals("Error")){
                    UsuarioSQLiteHelper helper = new UsuarioSQLiteHelper(ActividadPrincipal.this,"DBCLE",null, 1);
                    SQLiteDatabase db = helper.getWritableDatabase();

                        if(db !=null){

                            db.execSQL("INSERT INTO Usuario (nombres, apellido_p, apellido_m) " +
                                    "values("+o.getString("nombres")+","+o.getString("paterno")+","+o.getString("materno")+")");

                            db.close();
                        }
                    }
                    else{
                        Toast.makeText(ActividadPrincipal.this,"Error en las credenciales",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.data!=null) {
                    try {
                        String body = new String(error.networkResponse.data,"UTF-8");
                        Log.e("CLE",body);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                    params.put("pass", pass);
                    params.put("run", run);
                return params;
            }

/*            @Override
            public byte[] getBody() throws AuthFailureError {
                return parametros.toString().getBytes();
            }*/
/*                        @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap<String, String>();
                headers.put("Content-Length:", "30");
                return headers;
            }*/

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        CLESingleton.getInstance(this).addToRequestQueue(request);
        }
}

package mprz.cl.cle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import mprz.cl.cle.clases.CLESingleton;
import mprz.cl.cle.util.Constantes;

import static mprz.cl.cle.util.Constantes.URL;

public class ActividadPrincipal extends AppCompatActivity {

    Button btn_login;
    EditText et_user;
    EditText et_pass;

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

                //validarUsuario(et_user.getText().toString(), et_pass.getText().toString());
                if(et_user.getText().toString().equals("11111111-1") && et_pass.getText().toString().equals("juanperez")){

                    Intent i = new Intent(ActividadPrincipal.this, Inicio.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(ActividadPrincipal.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void validarUsuario(final String user, final String pass){

        StringRequest req = new StringRequest(Request.Method.POST,
                URL+"/Login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("CLE",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CLE",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap parametros = new HashMap<String, String>();
                    parametros.put("run", "11111111-1");
                    parametros.put("pass", "juanperez");
                return parametros;
            }

/*            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                headers.put("run", "11111111-1");
                headers.put("pass", "juanperez");
                return headers;
            }*/
/*            @Override
            public byte[] getBody() throws AuthFailureError {

                String postData = "run=11111111-1&pass=juanperez";

                try {
                    return postData == null ? null :
                            postData.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException uee) {
                    // TODO consider if some other action should be taken
                    return null;
                }
            }*/
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        CLESingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
        }
}

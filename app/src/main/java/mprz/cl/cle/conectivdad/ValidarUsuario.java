package mprz.cl.cle.conectivdad;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by elias on 28-09-15.
 */
public class ValidarUsuario extends AsyncTask<String, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {

        getDatos(params[0], params[1]);

        return null;
    }


    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

    public void getDatos(String usuario, String password) {

            String SOAP_ACTION = "http://www.w3schools.com/webservices/CelsiusToFahrenheit";
            String METHOD_NAME = "CelsiusToFahrenheit";
            String NAMESPACE = "http://www.w3schools.com/webservices/";
            String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";

            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("rut", usuario);
                Request.addProperty("password", password);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL);

                transport.call(SOAP_ACTION, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "Result Celsius: " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "Error: " + ex.getMessage());
            }

    }
}


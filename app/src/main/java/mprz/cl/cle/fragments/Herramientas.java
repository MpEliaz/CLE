package mprz.cl.cle.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import mprz.cl.cle.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Herramientas extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_herramientas, container, false);

        TextView img1 = (TextView)v.findViewById(R.id.link_comunicacion);
        img1.setOnClickListener(btnListener);
        TextView img2 = (TextView)v.findViewById(R.id.link_trabajo_en_equipo);
        img2.setOnClickListener(btnListener);

        return v;
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            TextView text = (TextView)v;
            String url = "";
            boolean browser = false;

            if(text.getText().equals("comunicacion")){
                url = "http://cle.ejercito.cl/cursos/Comunicacion.aspx";
                browser = true;
            }
            


            if(browser){
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }


        }
    };


}

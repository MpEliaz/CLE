package mprz.cl.cle.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mprz.cl.cle.R;
import mprz.cl.cle.util.SQLiteEncuestasHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class introEncuesta extends Fragment {

    private SQLiteEncuestasHandler db;
    private String cod_relacion;

    public static introEncuesta newInstance(String cod_relacion) {

        Bundle args = new Bundle();
        args.putString("cod_relacion", cod_relacion);
        introEncuesta fragment = new introEncuesta();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cod_relacion = getArguments().getString("cod_relacion");
        db = new SQLiteEncuestasHandler(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_intro_encuesta, container, false);

        String[] datos = db.obtenerIntroduccionAEncuesta(cod_relacion);

        TextView titulo_intro = (TextView)v.findViewById(R.id.titulo_intro);
        TextView intro = (TextView)v.findViewById(R.id.intro);
        TextView titulo_comp = (TextView)v.findViewById(R.id.titulo_comp);
        TextView comp = (TextView)v.findViewById(R.id.comp);
        TextView titulo_atrib = (TextView)v.findViewById(R.id.titulo_atrib);
        TextView atrib = (TextView)v.findViewById(R.id.atrib);

        titulo_intro.setText(datos[0]);
        intro.setText(datos[1]);
        titulo_comp.setText(datos[2]);
        comp.setText(datos[3]);
        titulo_atrib.setText(datos[4]);
        atrib.setText(datos[5]);

        return v;
    }

}

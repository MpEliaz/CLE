package mprz.cl.cle.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;

import mprz.cl.cle.R;
import mprz.cl.cle.util.SQLiteEncuestasHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class introEncuesta2 extends Fragment {

    private SQLiteEncuestasHandler db;
    private String cod_relacion;

    public static introEncuesta2 newInstance(String cod_relacion) {

        Bundle args = new Bundle();
        args.putString("cod_relacion", cod_relacion);
        introEncuesta2 fragment = new introEncuesta2();

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
        View v = inflater.inflate(R.layout.fragment_intro_encuesta2, container, false);

        String[] datos = db.obtenerIntroduccionAEncuesta(cod_relacion);

        TextView titulo_comp = (TextView)v.findViewById(R.id.titulo_comp);
        DocumentView comp = (DocumentView)v.findViewById(R.id.comp);

        titulo_comp.setText(datos[2]);
        comp.setText(datos[3]);

        return v;
    }

}

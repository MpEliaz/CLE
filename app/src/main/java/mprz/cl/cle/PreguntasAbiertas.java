package mprz.cl.cle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import mprz.cl.cle.R;
import mprz.cl.cle.util.SQLiteEncuestasHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreguntasAbiertas extends Fragment {

    private static final String RUN_EVALUADO = "run_evaluado";
    private static final String COD_RELACION = "cod_relacion";
    private SQLiteEncuestasHandler db;
    private EditText f1;
    private EditText f2;
    private EditText f3;
    private EditText m1;
    private EditText m2;
    private EditText m3;
    private EditText com;
    private String rut;
    private String cod_relacion;

    public static final PreguntasAbiertas newInstance(String run_Evaluado, String cod_relacion){

        PreguntasAbiertas p = new PreguntasAbiertas();
        Bundle args = new Bundle();
        args.putString(RUN_EVALUADO, run_Evaluado);
        args.putString(COD_RELACION, cod_relacion);
        p.setArguments(args);
        return p;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new SQLiteEncuestasHandler(getActivity());
        rut = getArguments().getString(RUN_EVALUADO);
        cod_relacion = getArguments().getString(COD_RELACION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_preguntas_abiertas, container, false);

        f1 = (EditText)v.findViewById(R.id.fortaleza1);
        f2 = (EditText)v.findViewById(R.id.fortaleza1);
        f3 = (EditText)v.findViewById(R.id.fortaleza1);
        m1 = (EditText)v.findViewById(R.id.mejora1);
        m2 = (EditText)v.findViewById(R.id.mejora2);
        m3 = (EditText)v.findViewById(R.id.mejora3);
        com = (EditText)v.findViewById(R.id.comentario);


        return v;
    }

    public boolean guardar(){
        boolean paso = true;

        if(f1.getText().toString().equals("")){paso= false;}
        if(f2.getText().toString().equals("")){paso= false;}
        if(f3.getText().toString().equals("")){paso= false;}
        if(m1.getText().toString().equals("")){paso= false;}
        if(m2.getText().toString().equals("")){paso= false;}
        if(m3.getText().toString().equals("")){paso= false;}
        if(com.getText().toString().equals("")){paso= false;}
        Log.i("CLE","PASE POR AQUI");

        if(paso){
            db.saveQuestionWithAnswer("fortaleza1", f1.getText().toString(), cod_relacion, rut);
            db.saveQuestionWithAnswer("fortaleza2", f2.getText().toString(), cod_relacion, rut);
            db.saveQuestionWithAnswer("fortaleza3", f3.getText().toString(), cod_relacion, rut);
            db.saveQuestionWithAnswer("mejora1", m1.getText().toString(), cod_relacion, rut);
            db.saveQuestionWithAnswer("mejora2", m2.getText().toString(), cod_relacion, rut);
            db.saveQuestionWithAnswer("mejora3", m3.getText().toString(), cod_relacion, rut);
            db.saveQuestionWithAnswer("comentario", com.getText().toString(), cod_relacion, rut);
        }

        return paso;
    }

}

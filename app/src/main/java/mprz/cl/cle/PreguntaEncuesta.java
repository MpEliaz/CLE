package mprz.cl.cle;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.Respuesta;

/**
 * Created by elias on 12-11-15.
 */
public class PreguntaEncuesta extends Fragment {

    public static final String PREGUNTA = "PREGUNTA";
    private Pregunta pregunta;

    public static final PreguntaEncuesta newIntance(Pregunta p){

        PreguntaEncuesta frag = new PreguntaEncuesta();
        Bundle bundle = new Bundle(1);
        bundle.putParcelable(PREGUNTA, p);
        frag.setArguments(bundle);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pregunta = getArguments().getParcelable(PREGUNTA);

        View v = inflater.inflate(R.layout.pregunta_encuesta, container, false);

        TextView titulo = (TextView)v.findViewById(R.id.pregunta_txt);
        titulo.setText(pregunta.getTitulo());

        RadioGroup rg = (RadioGroup)v.findViewById(R.id.rg_preg);

        RadioGroup.LayoutParams rprms;

        for (Respuesta r: pregunta.getRespuestas()) {

            RadioButton rb = new RadioButton(getContext());
            rb.setId(r.getId());
            int textColor = Color.parseColor("#000000");
            rb.setButtonTintList(ColorStateList.valueOf(textColor));
            rb.setText(r.getRespuesta());
            rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rg.addView(rb, rprms);



        }

        return v;
    }
}

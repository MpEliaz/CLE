package mprz.cl.cle;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.Respuesta;
import mprz.cl.cle.util.SQLiteHandler;

/**
 * Created by elias on 12-11-15.
 */
public class PreguntaEncuesta extends Fragment {

    public static final String PREGUNTA = "PREGUNTA";
    private Pregunta pregunta;
    private SQLiteHandler db;
   // private OnFragmentInteractionListener mListener;

    public static final PreguntaEncuesta newIntance(Pregunta p){

        PreguntaEncuesta frag = new PreguntaEncuesta();
        Bundle bundle = new Bundle(1);
        bundle.putParcelable(PREGUNTA, p);
        frag.setArguments(bundle);

        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new SQLiteHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pregunta = getArguments().getParcelable(PREGUNTA);

        View v = inflater.inflate(R.layout.item_pregunta_encuesta, container, false);

        TextView titulo = (TextView)v.findViewById(R.id.pregunta_txt);
        titulo.setText(pregunta.getTitulo());

        RadioGroup rg = (RadioGroup)v.findViewById(R.id.rg_preg);

        RadioGroup.LayoutParams rprms;

        for (Respuesta r: pregunta.getRespuestas()) {

            RadioButton rb = new RadioButton(getContext());
            rb.setId(r.getId());
            int textColor = Color.parseColor("#000000");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rb.setButtonTintList(ColorStateList.valueOf(textColor));
            }
            rb.setText(r.getRespuesta());
            rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rg.addView(rb, rprms);
        }


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                db.saveQuestionWithAnswer(pregunta.getId(), i);
                Toast.makeText(getActivity(), "presionado: respuesta" + i + " de pregunta:" + pregunta.getId(), Toast.LENGTH_SHORT).show();
            }
        });

/*        int radioButtonID = rg.getCheckedRadioButtonId();
        View radioButton = rg.findViewById(radioButtonID);
        int idx = radioButtonGroup.indexOfChild(radioButton);*/



        return v;
    }

/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/
}

package mprz.cl.cle.adaptadores;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mprz.cl.cle.R;
import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.Respuesta;

/**
 * Created by elias on 01-11-15.
 */
public class adaptadorEncuesta extends RecyclerView.Adapter<adaptadorEncuesta.EncuestaViewHolder> {

    private Context cx;
    private ArrayList<Pregunta> preguntas;

    public adaptadorEncuesta(Context cx, ArrayList<Pregunta> preguntas) {
        this.cx = cx;
        this.preguntas = preguntas;
    }

    @Override
    public EncuestaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View iteView = LayoutInflater.from(cx).inflate(R.layout.item_encuesta_1, parent,false);

        final EncuestaViewHolder vh = new EncuestaViewHolder(iteView);

        return vh;
    }

    @Override
    public void onBindViewHolder(EncuestaViewHolder holder, int position) {

        Pregunta p = preguntas.get(position);
        holder.bindPregunta(p);

    }

    @Override
    public int getItemCount() {
        if(preguntas != null){
            return preguntas.size();
        }
        return 0;
    }

    public class EncuestaViewHolder extends RecyclerView.ViewHolder {

        private int id;
        private TextView pregunta;
        private RadioGroup rg;

        public EncuestaViewHolder(View itemView) {
            super(itemView);

            pregunta = (TextView)itemView.findViewById(R.id.pregunta);
            rg = (RadioGroup)itemView.findViewById(R.id.grp_respuestas);



        }

        public void bindPregunta(final Pregunta p){

            id = p.getId();
            pregunta.setText(p.getTitulo());

            ArrayList<Respuesta> respuestas = p.getRespuestas();

            RadioGroup.LayoutParams rprms;

            for (Respuesta r: respuestas) {

                RadioButton rb = new RadioButton(cx);
                rb.setId(r.getId());
                int textColor = Color.parseColor("#000000");
                rb.setButtonTintList(ColorStateList.valueOf(textColor));
                rb.setText(r.getRespuesta());
                rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                rg.addView(rb,rprms);



            }


        }
    }
}

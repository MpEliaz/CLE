package mprz.cl.cle.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import mprz.cl.cle.ActividadPrincipal;
import mprz.cl.cle.R;
import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.showEncuesta;

/**
 * Created by elias on 26-10-15.
 */
public class adaptadorEncuestados extends RecyclerView.Adapter<adaptadorEncuestados.EncuestadosViewHolder> {

    private OnItemClickListener onItemClickListener;
    private ArrayList<Encuesta> datos;
    private static Context cx;

    public adaptadorEncuestados(ArrayList<Encuesta> datos, Context cx) {
        this.datos = datos;
        this.cx = cx;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Encuesta encuesta, int position);
    }

    @Override
    public adaptadorEncuestados.EncuestadosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_encuestados, parent, false);
        final EncuestadosViewHolder vh = new EncuestadosViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(view,datos.get(vh.getAdapterPosition()),vh.getAdapterPosition());
                }
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(EncuestadosViewHolder holder, int position) {
        Encuesta e = datos.get(position);
        holder.bindEncuestado(e);
    }

    @Override
    public int getItemCount() {
        if(datos != null){
            return datos.size();
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class EncuestadosViewHolder extends RecyclerView.ViewHolder {

        private String rut;
        private int id_encuesta;
        private TextView nombre;
        private TextView categoria;
        private Button btn;

        public EncuestadosViewHolder(View itemView) {
            super(itemView);

            rut = null;
            nombre = (TextView)itemView.findViewById(R.id.encuestado_nombre);
            categoria = (TextView)itemView.findViewById(R.id.encuestado_cat);
            btn = (Button)itemView.findViewById(R.id.btn_evaluar);
        }

        public void bindEncuestado(final Encuesta e){

            rut = e.getRunEvaluado();
            nombre.setText(e.getNombreEvaluado());
            categoria.setText(e.getRelacion());

            if(e.getEstado().equals("Finalizada")){
                btn.setText("Finalizado");
                btn.setEnabled(false);
                btn.setBackgroundColor(cx.getResources().getColor(R.color.grey));
            }
            else {
                btn.setText("Evaluar");
                btn.setEnabled(true);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(cx, showEncuesta.class);
                        i.putExtra("runEvaluado", e.getRunEvaluado());
                        i.putExtra("cod_relacion", e.getCod_relacion());
                        //cx.startActivity(i);
                        ((ActividadPrincipal)cx).startActivityForResult(i, 123);
                    }
                });
            }
        }
    }

    public void updateData(ArrayList<Encuesta> encuestas) {
        datos.clear();
        datos.addAll(encuestas);
        notifyDataSetChanged();
    }
}

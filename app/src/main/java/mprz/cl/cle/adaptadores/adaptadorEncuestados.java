package mprz.cl.cle.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import mprz.cl.cle.R;
import mprz.cl.cle.clases.Encuesta;

/**
 * Created by elias on 26-10-15.
 */
public class adaptadorEncuestados extends RecyclerView.Adapter<adaptadorEncuestados.EncuestadosViewHolder> {

    private OnItemClickListener onItemClickListener;
    private ArrayList<Encuesta> datos;

    public adaptadorEncuestados(ArrayList<Encuesta> datos) {
        this.datos = datos;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Encuesta encuesta, int position);
    }

    @Override
    public adaptadorEncuestados.EncuestadosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_encuesta, parent, false);

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

        public void bindEncuestado(Encuesta e){

            rut = e.getRunEvaluado();
            nombre.setText(e.getNombreEvaluado());
            categoria.setText(e.getRelacion());

            if(e.getEstado().equals("Finalizada")){
                btn.setText("Finalizado");
                btn.setEnabled(false);
            }
            else {
                btn.setText("Evaluar");
                btn.setEnabled(true);
            }

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        }
    }

    public void updateData(ArrayList<Encuesta> encuestas) {
        datos.clear();
        datos.addAll(encuestas);
        notifyDataSetChanged();
    }
}

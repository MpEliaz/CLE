package mprz.cl.cle.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mprz.cl.cle.R;
import mprz.cl.cle.clases.Persona;

/**
 * Created by elias on 26-10-15.
 */
public class adaptadorEncuestados extends RecyclerView.Adapter<adaptadorEncuestados.EncuestadosViewHolder>
implements View.OnClickListener{

    private View.OnClickListener listener;
    private ArrayList<Persona> datos;

    public adaptadorEncuestados(ArrayList<Persona> datos) {
        this.datos = datos;
    }

    @Override
    public adaptadorEncuestados.EncuestadosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_encuesta, parent, false);

        EncuestadosViewHolder vh = new EncuestadosViewHolder(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(EncuestadosViewHolder holder, int position) {
        Persona p = datos.get(position);
        holder.bindEncuestado(p);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public static class EncuestadosViewHolder extends RecyclerView.ViewHolder {

        private String rut;
        private TextView nombre;
        private TextView categoria;

        public EncuestadosViewHolder(View itemView) {
            super(itemView);

            rut = null;
            nombre = (TextView)itemView.findViewById(R.id.encuestado_nombre);
            categoria = (TextView)itemView.findViewById(R.id.encuestado_cat);
        }

        public void bindEncuestado(Persona p){

            rut = p.getRut();
            nombre.setText(p.getNombre());
            categoria.setText(p.getCategoria());
        }
    }
}

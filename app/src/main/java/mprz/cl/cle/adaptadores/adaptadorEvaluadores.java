package mprz.cl.cle.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mprz.cl.cle.R;
import mprz.cl.cle.clases.Documento;
import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Persona;

/**
 * Created by elias on 01-11-15.
 */
public class adaptadorEvaluadores extends RecyclerView.Adapter<adaptadorEvaluadores.personaViewHolder> {

    private Context cx;
    private ArrayList<Persona> personas;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemClickListener onItemClickListener;

    public adaptadorEvaluadores(Context cx, ArrayList<Persona> personas) {
        this.cx = cx;
        this.personas = personas;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Persona persona, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, Persona persona, int position);
    }

    public void setOnLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @Override
    public personaViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(cx).inflate(R.layout.item_evaluadores, parent,false);
        final adaptadorEvaluadores.personaViewHolder vh = new personaViewHolder(itemView);

        ImageButton btn = (ImageButton) itemView.findViewById(R.id.delete_evaluador);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(v, personas.get(vh.getAdapterPosition()),vh.getAdapterPosition());
                }
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(personaViewHolder holder, int position) {

        Persona p = personas.get(position);
        holder.bindPersona(p);
    }

    @Override
    public int getItemCount() {
        if(personas != null){
            return personas.size();
        }
        return 0;
    }

    public class personaViewHolder extends RecyclerView.ViewHolder {

        private int id;
        private TextView nombre;
        private TextView rut;
        private ImageButton delete;

        public personaViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView)itemView.findViewById(R.id.nombre_evaluador);
            rut = (TextView)itemView.findViewById(R.id.rut_evaluador);
            //delete = (ImageButton)itemView.findViewById(R.id.delete_evaluador);

        }

        public void bindPersona(final Persona p){
            nombre.setText(p.getNombre());
            rut.setText(p.getRut());


        }
    }

    public void updateData(ArrayList<Persona> data) {
        personas.clear();
        personas.addAll(data);
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        personas.remove(position);
        notifyItemRemoved(position);
    }
}

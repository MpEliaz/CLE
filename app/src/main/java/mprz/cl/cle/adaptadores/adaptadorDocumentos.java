package mprz.cl.cle.adaptadores;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mprz.cl.cle.R;
import mprz.cl.cle.clases.Documento;
import mprz.cl.cle.clases.Encuesta;
import mprz.cl.cle.clases.Pregunta;
import mprz.cl.cle.clases.Respuesta;

/**
 * Created by elias on 01-11-15.
 */
public class adaptadorDocumentos extends RecyclerView.Adapter<adaptadorDocumentos.DocumentoViewHolder> {

    private Context cx;
    private ArrayList<Documento> documentos;
    private OnItemClickListener onItemClickListener;

    public adaptadorDocumentos(Context cx, ArrayList<Documento> documentos) {
        this.cx = cx;
        this.documentos = documentos;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Documento documento, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public DocumentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(cx).inflate(R.layout.item_documento, parent,false);
        final adaptadorDocumentos.DocumentoViewHolder vh = new DocumentoViewHolder(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(view,documentos.get(vh.getAdapterPosition()),vh.getAdapterPosition());
                }
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(DocumentoViewHolder holder, int position) {

        Documento d = documentos.get(position);
        holder.bindPregunta(d);
    }

    @Override
    public int getItemCount() {
        if(documentos != null){
            return documentos.size();
        }
        return 0;
    }

    public class DocumentoViewHolder extends RecyclerView.ViewHolder {

        private int id;
        private TextView nombre;
        private ImageView imagen;

        public DocumentoViewHolder(View itemView) {
            super(itemView);

            nombre = (TextView)itemView.findViewById(R.id.titulo_doc);
            imagen = (ImageView)itemView.findViewById(R.id.img_doc);


        }

        public void bindPregunta(final Documento d){
            nombre.setText(d.getNombre());
            imagen.setImageResource(R.drawable.icon_document);



        }
    }
}

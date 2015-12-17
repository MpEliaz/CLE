package mprz.cl.cle.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import mprz.cl.cle.DocumentosPager;
import mprz.cl.cle.R;
import mprz.cl.cle.adaptadores.adaptadorDocumentos;
import mprz.cl.cle.clases.Documento;

/**
 * A simple {@link Fragment} subclass.
 */
public class Doctrina extends Fragment implements adaptadorDocumentos.OnItemClickListener{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctrina, container, false);

        RecyclerView rv = (RecyclerView)v.findViewById(R.id.rv_doctrina_docs);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        Documento d = new Documento();
        adaptadorDocumentos adapter = new adaptadorDocumentos(getActivity(), d.datos_ejemplo());
        adapter.setOnItemClickListener(this);
        rv.setAdapter(adapter);

        return v;
    }

    @Override
    public void onItemClick(View view, Documento documento, int position) {

        Toast.makeText(getActivity(), "nombre: " + documento.getNombre(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getActivity(), DocumentosPager.class);
        i.putExtra("nombres",documento.getPaginas());
        startActivity(i);
    }


}

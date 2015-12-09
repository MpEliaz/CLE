package mprz.cl.cle;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mprz.cl.cle.R;
import mprz.cl.cle.adaptadores.adaptadorDocumentos;
import mprz.cl.cle.clases.Documento;

/**
 * A simple {@link Fragment} subclass.
 */
public class Doctrina extends Fragment {

    private adaptadorDocumentos adapter;


    public Doctrina() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctrina, container, false);

        RecyclerView rv = (RecyclerView)v.findViewById(R.id.rv_doctrina_docs);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        Documento d = new Documento();
        adapter = new adaptadorDocumentos(getActivity(), d.datos_ejemplo());

        rv.setAdapter(adapter);

        return v;
    }


}

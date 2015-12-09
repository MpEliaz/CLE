package mprz.cl.cle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mprz.cl.cle.R;
import mprz.cl.cle.adaptadores.adaptadorNoticiasHome;
import mprz.cl.cle.clases.Noticia;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    private RecyclerView rv;
    private adaptadorNoticiasHome adapter;
    private ArrayList<Noticia> noticias;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Noticia a = new Noticia();
        noticias = new ArrayList<>();
        adapter = new adaptadorNoticiasHome(getActivity(), a.datos_ejemplo());
        rv = (RecyclerView)v.findViewById(R.id.noticias_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);

        return v;
    }


}

package mprz.cl.cle.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import mprz.cl.cle.R;
import mprz.cl.cle.clases.CLESingleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link paginaDocumento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class paginaDocumento extends Fragment {

    private static final String NOMBRE_IMAGEN = "nombre_imagen";
    private ImageView image;
    private Bitmap bitmap;


    private String nombre_imagen;

    public static paginaDocumento newInstance(String nombre) {
        paginaDocumento fragment = new paginaDocumento();
        Bundle args = new Bundle();
        args.putString(NOMBRE_IMAGEN, nombre);
        fragment.setArguments(args);
        return fragment;
    }

    public paginaDocumento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nombre_imagen = getArguments().getString(NOMBRE_IMAGEN);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pagina_documento, container, false);

        image = (ImageView)v.findViewById(R.id.img_documento);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int resId = getResources().getIdentifier(nombre_imagen , "drawable", getActivity().getPackageName());
        image.setImageResource(resId); // Load image into ImageView
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bitmap.recycle();
        bitmap = null;
    }
}

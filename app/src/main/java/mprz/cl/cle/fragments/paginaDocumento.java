package mprz.cl.cle.fragments;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

        loadImage load = new loadImage(image,nombre_imagen);
        load.execute();
        return v;
    }

    class loadImage extends AsyncTask<Void, Void, Bitmap>{

        ImageView image;
        String nombre;
        ProgressDialog progressDialog;

        public loadImage(ImageView i, String nombre) {
            image = i;
            this.nombre = nombre;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            int resId = getResources().getIdentifier(nombre, "drawable", getActivity().getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), resId);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            image.setImageBitmap(bitmap);
            progressDialog.hide();
        }
    }

}

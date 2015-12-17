package mprz.cl.cle.fragments;


import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import mprz.cl.cle.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A simple {@link Fragment} subclass.
 */
public class Organica extends Fragment {

    PhotoViewAttacher mAttacher;
    private ImageView mImageView;

    public Organica() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_organica, container, false);

    mImageView = (ImageView)v.findViewById(R.id.organica_img);
        Drawable bitmap = getResources().getDrawable(R.drawable.organigrama);
        mImageView.setImageDrawable(bitmap);

        mAttacher = new PhotoViewAttacher(mImageView);

        return v;
    }


}

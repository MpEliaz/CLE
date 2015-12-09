package mprz.cl.cle.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;

import mprz.cl.cle.PreguntaEncuesta;
import mprz.cl.cle.clases.Pregunta;

/**
 * Created by elias on 12-11-15.
 */
public class adaptadorEncuestaPager extends FragmentPagerAdapter {

    ArrayList<PreguntaEncuesta> preguntas;

    public adaptadorEncuestaPager(FragmentManager fm, ArrayList<PreguntaEncuesta> preguntas) {
        super(fm);
        this.preguntas = preguntas;
    }

    @Override
    public int getCount() {
        return this.preguntas.size();

    }

    @Override
    public Fragment getItem(int position) {
        return preguntas.get(position);
    }

    public boolean verificarCheck(){

        return false;
    }



}

package mprz.cl.cle.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mprz.cl.cle.fragments.paginaDocumento;

/**
 * Created by Elias Millachine on 16-12-2015.
 */
public class adaptadorPagerDocumentos extends FragmentStatePagerAdapter {

    String[] nombres;

    public adaptadorPagerDocumentos(FragmentManager fm, String[] nombres) {
        super(fm);
        this.nombres = nombres;
    }

    @Override
    public int getCount() {
        return this.nombres.length;

    }

    @Override
    public Fragment getItem(int position) {
        return paginaDocumento.newInstance(nombres[position]);
    }


}

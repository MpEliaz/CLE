package mprz.cl.cle.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
        return 3;

    }

    @Override
    public Fragment getItem(int position) {
        return paginaDocumento.newInstance(nombres[position]);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
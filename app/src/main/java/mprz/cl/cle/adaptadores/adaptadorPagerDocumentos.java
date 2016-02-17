package mprz.cl.cle.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

import mprz.cl.cle.R;
import mprz.cl.cle.fragments.paginaDocumento;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Elias Millachine on 16-12-2015.
 */
public class adaptadorPagerDocumentos extends PagerAdapter {

    String[] nombres;
    int[] ids = {R.drawable.competencias_pag_01_,
                R.drawable.competencias_pag_02_,
                R.drawable.competencias_pag_03_,
                R.drawable.competencias_pag_04_,
                R.drawable.competencias_pag_05_,
                R.drawable.competencias_pag_06_,
                R.drawable.competencias_pag_07_,
                R.drawable.competencias_pag_08_,
                R.drawable.competencias_pag_09_,
                R.drawable.competencias_pag_10_,
                R.drawable.competencias_pag_11_,
                R.drawable.competencias_pag_12_,
                R.drawable.competencias_pag_13_,
                R.drawable.competencias_pag_14_,
                R.drawable.competencias_pag_15_,
                R.drawable.competencias_pag_16_,
                R.drawable.competencias_pag_17_,
                R.drawable.competencias_pag_18_,
                R.drawable.competencias_pag_19_,
                R.drawable.competencias_pag_20_,
                R.drawable.competencias_pag_21_,
                R.drawable.competencias_pag_22_,
                R.drawable.competencias_pag_23_,
    };


    public adaptadorPagerDocumentos(String[] nombres) {
        this.nombres = nombres;
    }

    @Override
    public int getCount() {
        return ids.length;

    }


    @Override
    public View instantiateItem(ViewGroup container, int position) {

        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setImageResource(ids[position]);

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

/*    @Override
    public Fragment getItem(int position) {
        return paginaDocumento.newInstance(nombres[position]);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }*/
}

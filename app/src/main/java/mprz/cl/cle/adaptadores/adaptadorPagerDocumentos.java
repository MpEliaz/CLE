package mprz.cl.cle.adaptadores;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import mprz.cl.cle.R;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Elias Millachine on 16-12-2015.
 */
public class adaptadorPagerDocumentos extends PagerAdapter {

    int[] ids;
    Context cx;

    public adaptadorPagerDocumentos(int[] ids, Context cx) {
        this.ids = ids;
        this.cx = cx;
    }

    @Override
    public int getCount() {
        return ids.length;

    }


    @Override
    public View instantiateItem(ViewGroup container, int position) {

        PhotoView photoView = new PhotoView(container.getContext());
        Bitmap bitmap = BitmapFactory.decodeResource(cx.getResources(), ids[position]);
        photoView.setImageBitmap(bitmap);

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("DESTROY", "destroying view at position " + position);
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

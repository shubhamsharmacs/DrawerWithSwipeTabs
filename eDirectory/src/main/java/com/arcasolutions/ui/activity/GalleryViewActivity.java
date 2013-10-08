package com.arcasolutions.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Photo;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class GalleryViewActivity extends ActionBarActivity {

    public static final String EXTRA_PHOTO_ARRAY = "photo_array";
    public static final String EXTRA_PHOTO_INDEX = "photo_index";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gallery_view);

        ArrayList<Photo> gallery = getIntent().getParcelableArrayListExtra(EXTRA_PHOTO_ARRAY);
        int index = getIntent().getIntExtra(EXTRA_PHOTO_INDEX, 0);

        if (gallery != null) {
            GalleryViewAdapter adapter = new GalleryViewAdapter(gallery);
            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(adapter);
            pager.setOffscreenPageLimit(gallery.size());
            pager.setCurrentItem(index);
        }
    }

    static class GalleryViewAdapter extends PagerAdapter {
        private ArrayList<Photo> mGallery;

        GalleryViewAdapter(ArrayList<Photo> gallery) {
            mGallery = gallery;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setBackgroundColor(Color.argb(200, 0, 0, 0));


            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            new AQuery(photoView).image(mGallery.get(position).getImageUrl(), true, true);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            if (mGallery == null) return 0;
            return mGallery.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }
}

package com.arcasolutions.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.weedfinder.R;
import com.arcasolutions.api.model.Photo;

import java.util.List;

public class GalleryAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final List<Photo> mPhotos;
    private final int mSize;

    public GalleryAdapter(Context context, List<Photo> objects, int size) {
        Context mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mPhotos = objects;
        mSize = size;
    }

    @Override
    public int getCount() {
        if (mPhotos == null) return 0;

        return mPhotos.size();
    }

    @Override
    public Photo getItem(int i) {
        if (mPhotos == null) return null;

        return mPhotos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.simple_grid_item_gallery, parent, false);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = mSize;
            params.height = mSize;
            view.setLayoutParams(params);
        }

        Photo photo = getItem(position);
        if (photo != null) {
            AQuery aq = new AQuery(view);
            aq.id(R.id.imageView)
                    .image(photo.getImageUrl(), true, true);
        }
        return view;
    }

}

package com.arcasolutions.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.weedfinder.R;
import com.arcasolutions.api.model.Photo;
import com.arcasolutions.ui.activity.GalleryViewActivity;
import com.arcasolutions.ui.adapter.GalleryAdapter;

import java.util.ArrayList;

public class GalleryFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String ARG_PHOTO_ARRAY = "photo_array";


    public GalleryFragment() {
    }

    public static GalleryFragment newInstance(ArrayList<Photo> gallery) {
        final Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PHOTO_ARRAY, gallery);
        final GalleryFragment f = new GalleryFragment();
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);

        DisplayMetrics metrics = new DisplayMetrics();
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        defaultDisplay.getMetrics(metrics);

        int size = (int) Math.ceil(metrics.widthPixels / getResources().getInteger(R.integer.numColumnsGallery));

        ArrayList<Photo> gallery = getShownGallery();

        GalleryAdapter adapter = new GalleryAdapter(getActivity(), gallery, size);
        gridView.setAdapter(adapter);
    }

    private ArrayList<Photo> getShownGallery() {
        Bundle args = getArguments();

        ArrayList<Photo> gallery = null;
        if (args != null) gallery = args.getParcelableArrayList(ARG_PHOTO_ARRAY);
        return gallery;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Photo photo = (Photo) adapterView.getItemAtPosition(i);
        if (photo != null) {

            Intent intent = new Intent(getActivity(), GalleryViewActivity.class);
            intent.putExtra(GalleryViewActivity.EXTRA_PHOTO_ARRAY, getShownGallery());
            intent.putExtra(GalleryViewActivity.EXTRA_PHOTO_INDEX, i);

            Rect rect = new Rect();
            view.requestRectangleOnScreen(rect, true);

            int x = rect.left;
            int y = rect.top;


            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

            if (drawable != null) {
                bitmap = drawable.getBitmap();
            }

            Bundle opts = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(view, bitmap, x, y)
                    .toBundle();

            ActivityCompat.startActivity(getActivity(), intent, opts);
        }
    }
}

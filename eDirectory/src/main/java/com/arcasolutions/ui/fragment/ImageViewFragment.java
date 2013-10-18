package com.arcasolutions.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Photo;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageViewFragment extends Fragment {

    public static final String ARG_PHOTO = "photo";
    private PhotoViewAttacher mAttacher;

    public ImageViewFragment() {
    }

    public static ImageViewFragment newInstance(Photo photo) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_PHOTO, photo);

        final ImageViewFragment f = new ImageViewFragment();
        f.setArguments(args);
        return f;
    }

    public Photo getShownPhoto() {
        return getArguments() != null
                ? (Photo) getArguments().getParcelable(ARG_PHOTO)
                : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getShownPhoto() != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            AQuery aq = new AQuery(imageView);
            aq.image(getShownPhoto().getImageUrl(), true, true);
            mAttacher = new PhotoViewAttacher(imageView);
            mAttacher.update();
        }
    }
}

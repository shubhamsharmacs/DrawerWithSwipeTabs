package com.arcasolutions.api.model;

import android.os.Parcelable;
import android.webkit.URLUtil;

import java.util.ArrayList;

public abstract class Module implements Parcelable {
    public abstract long getId();

    public ArrayList<Photo> getGallery() {
        return null;
    }

    public ArrayList<Photo> getIGallery() {
        if (getGallery() == null) return null;

        ArrayList<Photo> igallery = new ArrayList<Photo>();
        for (Photo p : getGallery()) {
            if (URLUtil.isValidUrl(p.getImageUrl())) {
                igallery.add(p);
            }
        }
        return igallery;
    }
}

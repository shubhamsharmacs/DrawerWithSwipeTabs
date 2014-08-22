package com.arcasolutions.api.model;

import android.os.Parcelable;
import android.webkit.URLUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Module implements Parcelable {
    public abstract long getId();

    public abstract String getListingId();

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

    public abstract int getLevel();

    /**
     * Retorna mapa de/para, onde 'de' eh o nome do atributo que
     * vem da API e 'para' eh o nome do atributo da classe.
     *
     * @return
     */
    public abstract Map<String, String> getLevelFieldsMap();

    public void keepGeneralFields(List<String> general) {
        if (general == null) return;
        if (getLevelFieldsMap() == null) return;

        for (Map.Entry<String, String> entry : getLevelFieldsMap().entrySet()) {
            if (!general.contains(entry.getKey())) {
                try {
                    Field field = getClass().getDeclaredField(entry.getValue());
                    field.setAccessible(true);
                    field.set(this, null);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

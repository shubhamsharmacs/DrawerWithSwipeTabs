package com.arcasolutions.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.arcasolutions.database.Database;
import com.j256.ormlite.dao.Dao;

import java.util.List;

public class FavoriteHelper<T> {

    private static final String TAG = "FavoriteHelper";

    public static final String ACTION_FAVORITE_CHANGED = "favorite_changed";
    private final Intent INTENT = new Intent(ACTION_FAVORITE_CHANGED);

    private final Context mContext;
    private final Database mDatabase;
    private final Class<T> mClazz;

    public FavoriteHelper(Context context, Class<T> clazz) {
        mContext = context;
        mDatabase = new Database(mContext);
        mClazz = clazz;
    }

    public boolean toggleFavorite(T module) {
        try {
            if (isFavorited(module)) {
                return destroy(module);
            } else {
                return createOrUpdate(module);
            }
        } catch (Exception ignored) {
            Log.e(TAG, ignored.getMessage(), ignored);
            return false;
        }
    }

    public void updateFavorite(T module) {
        if (isFavorited(module)) {
            createOrUpdate(module);
        }
    }

    public boolean destroy(List<Long> ids) {
        try {
            Dao<T, Long> dao = mDatabase.getDao(mClazz);
            dao.deleteIds(ids);
            notifyChanges();
            return true;
        } catch (Exception ignored) {
            Log.e(TAG, ignored.getMessage(), ignored);
            return false;
        }
    }

    private boolean createOrUpdate(T module) {
        try {
            Dao<T, Long> dao = mDatabase.getDao(mClazz);
            dao.createOrUpdate(module);
            notifyChanges();
            return true;
        } catch (Exception ignored) {
            Log.e(TAG, ignored.getMessage(), ignored);
            return false;
        }
    }

    private boolean destroy(T module) {
        try {
            Dao<T, Long> dao = mDatabase.getDao(mClazz);
            dao.delete(module);
            notifyChanges();
            return true;
        } catch (Exception ignored) {
            Log.e(TAG, ignored.getMessage(), ignored);
            return false;
        }
    }

    public boolean isFavorited(T module) {
        try {
            Dao<T, Long> dao = mDatabase.getDao(mClazz);
            T item = dao.queryForSameId(module);
            return item != null;
        } catch (Exception ignored) {
            Log.e(TAG, ignored.getMessage(), ignored);
            return false;
        }
    }

    public List<T> getFavorites() {
        try {
            Dao<T, Long> dao = mDatabase.getDao(mClazz);
            return dao.queryForAll();
        } catch (Exception ignored) {
            Log.e(TAG, ignored.getMessage(), ignored);
            return null;
        }
    }

    private void notifyChanges() {
        mContext.sendBroadcast(INTENT);
    }

}

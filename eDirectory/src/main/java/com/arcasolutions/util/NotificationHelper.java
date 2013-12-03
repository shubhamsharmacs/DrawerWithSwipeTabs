package com.arcasolutions.util;

import android.content.Context;
import android.util.Log;

import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.Notification;
import com.arcasolutions.api.model.NotificationResult;
import com.arcasolutions.database.Database;
import com.j256.ormlite.dao.Dao;

import java.util.List;

public class NotificationHelper implements Client.RestListener<NotificationResult> {

    private static final String TAG = "NotificationHelper";

    private static NotificationHelper mInstance;
    private Context mContext;
    // Runs once by execution
    private boolean ran = false;

    private NotificationHelper(Context context) {
        mContext = context;
    }

    public static NotificationHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NotificationHelper(context);
        }
        return mInstance;
    }

    public void checkNewNotifications() {
        if (ran) return; // abort if is already run in this session

        loadServerNotifications();
    }

    private void loadServerNotifications() {
        ran = true;
        new Client.Builder(NotificationResult.class)
                .execAsync(this);
    }

    private void handleNotifications(List<Notification> notifications) {
        if (notifications == null) return;

        try {
            Database db = new Database(mContext);
            Dao<Notification, Long> dao = db.getDao(Notification.class);

            Notification notification = null;
            for (Notification n : notifications) {
                notification = dao.queryForSameId(n);
                if (notification == null) {
                    notification =  dao.createIfNotExists(n);
                } else {
                    notification = null;
                }
            }

            if (notification != null) {
                DialogHelper.from(mContext)
                        .alert(notification.getTitle(), notification.getDescription());
            }

        } catch (Exception ignored) {
            Log.e(TAG, ignored.getMessage(), ignored);
        }
    }


    @Override
    public void onComplete(NotificationResult result) {
        List<Notification> notifications = result != null
                ? result.getResults()
                : null;
        handleNotifications(notifications);
    }

    @Override
    public void onFail(Exception ignored) {
        Log.e(TAG, ignored.getMessage(), ignored);
    }
}

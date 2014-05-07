package com.arcasolutions.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.weedfinder.R;
import com.arcasolutions.api.implementation.IGeoPoint;
import com.arcasolutions.api.model.ArticleResult;
import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.ClassifiedResult;
import com.arcasolutions.api.model.DealResult;
import com.arcasolutions.api.model.EventResult;
import com.arcasolutions.api.model.ListingResult;
import com.arcasolutions.api.model.Module;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Util {

    private static Location mMyLocation;

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static double distanceFromMe(double latitude, double longitude) {
        if (mMyLocation == null) return -1;
        Location l = new Location(LocationManager.PASSIVE_PROVIDER);
        l.setLatitude(latitude);
        l.setLongitude(longitude);
        return mMyLocation.distanceTo(l);
    }

    public static Location getMyLocation() {
        return mMyLocation;
    }

    public static void setMyLocation(Location myLocation) {
        mMyLocation = myLocation;
    }

    public static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public static void orderByDistance(LinkedList<Module> modules) {
        if (modules == null) return;

        Collections.sort(modules, new Comparator<Module>() {
            @Override
            public int compare(Module _l, Module _r) {
                if (_l instanceof IGeoPoint && _r instanceof IGeoPoint) {
                    IGeoPoint l = (IGeoPoint) _l;
                    IGeoPoint r = (IGeoPoint) _r;

                    double ld = Util.distanceFromMe(l.getLatitude(), l.getLongitude());
                    double rd = Util.distanceFromMe(r.getLatitude(), r.getLongitude());

                    if (ld < rd) return -1;
                    else if (ld > rd) return 1;
                }
                return 0;
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))
                + listView.getResources().getDimensionPixelSize(R.dimen.spacingMedium);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static boolean isNonLocationApp(Context context) {
        int defaultHomeScreen = context.getResources()
                .getInteger(R.integer.defaultHomeScreen);
        return defaultHomeScreen != 0;
    }

    public static Class<? extends BaseResult> getHomeClassResult(Context context) {
        int defaultHomeScreen = context.getResources()
                .getInteger(R.integer.defaultHomeScreen);

        Class<? extends BaseResult> homeBaseResult = ListingResult.class;

        switch (defaultHomeScreen) {
            case 1: // Listing
                homeBaseResult = ListingResult.class;
                break;

            case 2: // Deal
                homeBaseResult = DealResult.class;
                break;

            case 3: // Article
                homeBaseResult = ArticleResult.class;
                break;

            case 4: // Classified
                homeBaseResult = ClassifiedResult.class;
                break;

            case 5: // Event
                homeBaseResult = EventResult.class;
                break;
        }

        return homeBaseResult;
    }

    public interface ConnectionCallback {
        void onConnection(boolean hasInternet);
    }

    public static void verifyIntenetConnection(final Context context, final ConnectionCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... voids) {
                String domain = context.getString(R.string.edirectoryUrl);
                try {
                    return InetAddress.getByName(domain).isReachable(5 * 1000);
                } catch (Exception ignored) {}
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                callback.onConnection(aBoolean);
            }
        }.execute();

    }
}

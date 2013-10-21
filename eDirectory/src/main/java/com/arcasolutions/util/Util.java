package com.arcasolutions.util;

import android.location.Location;
import android.location.LocationManager;
import android.os.Build;

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

}

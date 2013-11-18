package com.arcasolutions.util;

import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;

import com.arcasolutions.api.implementation.IGeoPoint;
import com.arcasolutions.api.model.Module;

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
        if (TextUtils.isEmpty(email)) return false;

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

}

package com.arcasolutions.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocationUtil {

    public static void attach(TextView field, long maxDelay) {
        Location location = Util.getMyLocation();
        if (location == null) return;

        new LocationTask(maxDelay, field, location).execute();
    }

    public interface GeocoderCallback {
        void onLatLng(LatLng point);
    }

    public static void geocoder(Context context, String location, GeocoderCallback callback) {
        new GeocoderTask(context, callback).execute(location);
    }

    static class GeocoderTask extends AsyncTask<String, Void, Address> {

        private final Context mContext;
        private final GeocoderCallback mCallback;

        GeocoderTask(Context context, GeocoderCallback callback) {
            mCallback = callback;
            mContext = context;
        }

        @Override
        protected Address doInBackground(String... locations) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            try {
                List<Address> addresss = geocoder.getFromLocationName(locations[0], 1);
                if (addresss != null && !addresss.isEmpty()) {
                    return addresss.get(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Address address) {
            if (address == null) {
                mCallback.onLatLng(null);
            } else {
                double lat = address.getLatitude();
                double lng = address.getLongitude();
                mCallback.onLatLng(new LatLng(lat, lng));
            }
        }
    }

    static class LocationTask extends AsyncTask<Void, Void, Address> {

        private final long mMaxDelay;
        private final Context mContext;
        private final TextView mTextView;
        private final Location mLocation;

        private long mStartedAt;
        private long mEndedAt;

        LocationTask(long maxDelay, TextView textView, Location location) {
            mMaxDelay = maxDelay;
            mTextView = textView;
            mContext = mTextView.getContext();
            mLocation = location;
        }

        @Override
        protected void onPreExecute() {
            mStartedAt = System.currentTimeMillis();
        }

        @Override
        protected Address doInBackground(Void... voids) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            try {
                List<Address> addresss = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                if (addresss != null && !addresss.isEmpty()) {
                    return addresss.get(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Address address) {
            mEndedAt = System.currentTimeMillis();
            if (mEndedAt - mStartedAt > mMaxDelay) return;
            if (address == null) return;

            List<String> texts = Arrays.asList(
                    address.getLocality(),
                    address.getAdminArea(),
                    address.getCountryCode()
            );

            mTextView.setText(TextUtils.join(", ", texts));
        }
    }

}

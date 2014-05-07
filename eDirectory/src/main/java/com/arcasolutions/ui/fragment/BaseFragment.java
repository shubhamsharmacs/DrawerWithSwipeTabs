package com.arcasolutions.ui.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.weedfinder.R;
import com.arcasolutions.api.implementation.IGeoPoint;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public abstract class BaseFragment extends Fragment {

    private ShareActionProvider mShareActionProvider;
    private MenuItem mShareMenuItem;
    private Intent mShareIntent;
    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        try {
            MapsInitializer.initialize(getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (!menuVisible) {
            finishProgress();
        }
    }

    protected void startProgress() {
        if (getActivity() == null) return;
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    protected void finishProgress() {
        if (getActivity() == null) return;
        getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_share, menu);

        mShareMenuItem = menu.findItem(R.id.action_share);
        if (mShareMenuItem != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(mShareMenuItem);
            mShareActionProvider.onCreateActionView();
            if (mShareIntent != null) mShareActionProvider.setShareIntent(mShareIntent);
            mShareMenuItem.setVisible(mShareIntent != null);
        }
    }

    protected void doShare(Intent shareIntent) {
        if (shareIntent == null) return;

        mShareIntent = shareIntent;

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }

        if (mShareMenuItem != null) {
            mShareMenuItem.setVisible(true);
        }
    }

    protected void showMapIfNeeded(final IGeoPoint point) {
        if (point == null) return;

        Resources res = getResources();
        boolean isNonLocation = res.getBoolean(R.bool.hasMapOnDetail);
        if (!isNonLocation) return;

        final LatLng center = new LatLng(point.getLatitude(), point.getLongitude());

        View mapPlaceView = getView().findViewById(R.id.mapPlace);
        if (mapPlaceView != null) mapPlaceView.setVisibility(View.VISIBLE);

        GoogleMapOptions options = new GoogleMapOptions()
                .camera(CameraPosition.fromLatLngZoom(center, 16f))
                .zoomControlsEnabled(false)
                .zoomGesturesEnabled(false)
                .rotateGesturesEnabled(false)
                .scrollGesturesEnabled(false)
                .tiltGesturesEnabled(false);
        final SupportMapFragment fragment = SupportMapFragment.newInstance(options);
        getFragmentManager().beginTransaction()
                .replace(R.id.mapPlace, fragment, "map")
                .commit();

        final MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                .title(point.getTitle())
                .position(center);

        new Thread(new Runnable() {
            @Override
            public void run() {

                do {
                    map = fragment.getMap();
                    if (map == null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {
                        }
                    }
                } while (map == null);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        map.addMarker(markerOptions);
                    }
                });

            }
        }).start();


    }


}

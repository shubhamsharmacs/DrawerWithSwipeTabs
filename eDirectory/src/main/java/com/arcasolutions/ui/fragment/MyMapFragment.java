package com.arcasolutions.ui.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.implementation.IMapItem;
import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.EventResult;
import com.arcasolutions.ui.activity.BaseActivity;
import com.arcasolutions.ui.activity.listing.ListingResultActivity;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MyMapFragment extends Fragment implements GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, View.OnClickListener {

    private BaseActivity mBaseActivity;
    private GoogleMap mMap;

    private LatLng mSearchNearLeftLatLng;
    private LatLng mSearchFarRightLatLng;

    private Class<? extends BaseResult> mClass = EventResult.class;

    private final Map<Marker, IMapItem> mListingMap = Maps.newHashMap();

    public MyMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivity = (BaseActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        final View mapInfo = view.findViewById(R.id.mapInfoView);
        mapInfo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int h = mapInfo.getVisibility() == View.VISIBLE
                        ? mapInfo.getHeight()
                        : 0;
                mMap.setPadding(0, 0, 0, h);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AQuery aq = new AQuery(view);
        aq.id(R.id.buttonList).clicked(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        LatLng center = getMyPosition();
        if (center != null) mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 15f));

    }

    private LatLng getMyPosition() {
        LocationClient locationClient = mBaseActivity.getLocationClient();
        if (locationClient.isConnected()) {
            Location location = locationClient.getLastLocation();
            if (location != null) {
                return new LatLng(location.getLatitude(), location.getLongitude());
            }
        }
        return null;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Projection projection = mMap.getProjection();
        VisibleRegion visibleRegion = projection.getVisibleRegion();
        mSearchNearLeftLatLng = visibleRegion.nearLeft;
        mSearchFarRightLatLng = visibleRegion.farRight;
        searchItems(mClass);
    }


    private void updateMapMarkers(Collection<IMapItem> items) {
        if (items == null) {
            mListingMap.clear();
            mMap.clear();
        }

        // Ignore existing items
        List<IMapItem> ignore = Lists.newArrayList();
        for (IMapItem l : items) {
            if (mListingMap.containsValue(l)) {
                ignore.add(l);
            }
        }
        items.removeAll(ignore);

        // Remove unneeded items
        List<Marker> unneeded = Lists.newArrayList();
        for (Map.Entry<Marker, IMapItem> entry : mListingMap.entrySet()) {
            if (!items.contains(entry.getValue()) && !ignore.contains(entry.getValue())) {
                unneeded.add(entry.getKey());
                entry.getKey().remove();
            }
        }
        for (Marker m : unneeded) {
            mListingMap.remove(m);
        }


        // Adds news items as Marker
        for (IMapItem l : items) {
            LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
            Marker m = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .position(latLng));
            mListingMap.put(m, l);
        }


    }

    private <T extends BaseResult> void searchItems(Class<T> tClass) {
        if (mSearchFarRightLatLng == null || mSearchNearLeftLatLng == null) return;

        Client.RestListener<T> mListener = new Client.RestListener<T>() {
            @Override
            public void onComplete(T result) {
                List listings = result.getResults();
                if (listings != null) {
                    Collection<IMapItem> items = Collections2.transform(listings, new Function<Object, IMapItem>() {
                        @Override
                        public IMapItem apply(Object listing) {
                            return (IMapItem) listing;
                        }
                    });
                    updateMapMarkers(items);
                }
            }

            @Override
            public void onFail(Exception ex) {
                ex.printStackTrace();
                Toast.makeText(mBaseActivity, "Fail: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        new Client.Builder(tClass)
                .searchBy(SearchBy.MAP)
                .region(
                        mSearchNearLeftLatLng.latitude,
                        mSearchNearLeftLatLng.longitude,
                        mSearchFarRightLatLng.latitude,
                        mSearchFarRightLatLng.longitude)
                .execAsync(mListener);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        IMapItem item = mListingMap.get(marker);
        if (item != null) {
            AQuery aq = new AQuery(getView());
            aq.id(R.id.mapInfoView).gone();
            aq.id(R.id.mapInfoTitle).text(item.getTitle());
            aq.id(R.id.mapInfoRatingBar).rating(item.getRating());
            aq.id(R.id.mapInfoDistance).text("2.2 miles");
            aq.id(R.id.mapInfoAddress).text(item.getAddress());
            aq.id(R.id.mapInfoView).visible().animate(android.R.anim.fade_in);
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        AQuery aq = new AQuery(getView());
        aq.id(R.id.mapInfoView).gone();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonList:
                ArrayList<Parcelable> items = new ArrayList<Parcelable>();
                for (Object i : mListingMap.values()) {
                    items.add((Parcelable) i);
                }
                Intent intent = new Intent(getActivity(), ListingResultActivity.class);
                intent.putExtra(ListingResultActivity.EXTRA_ITEMS, items);
                startActivity(intent);
                break;
        }
    }
}


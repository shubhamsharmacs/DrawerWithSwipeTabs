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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.implementation.IGeoPoint;
import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.ClassifiedResult;
import com.arcasolutions.api.model.DealResult;
import com.arcasolutions.api.model.EventResult;
import com.arcasolutions.api.model.ListingResult;
import com.arcasolutions.ui.activity.BaseActivity;
import com.arcasolutions.ui.activity.listing.ListingResultActivity;
import com.arcasolutions.util.Util;
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

public class MyMapFragment extends Fragment implements
        GoogleMap.OnCameraChangeListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private BaseActivity mBaseActivity;
    private GoogleMap mMap;

    private LatLng mSearchNearLeftLatLng;
    private LatLng mSearchFarRightLatLng;

    private Class<? extends BaseResult> mClass = EventResult.class;

    private final Map<Marker, IGeoPoint> mListingMap = Maps.newHashMap();

    public MyMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivity = (BaseActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);


        final View mapInfo = rootView.findViewById(R.id.mapInfoView);
        mapInfo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int h = mapInfo.getVisibility() == View.VISIBLE
                        ? mapInfo.getHeight()
                        : 0;
                mMap.setPadding(0, 0, 0, h);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Spinner adapter modules
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.modules,
                R.layout.simple_map_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_map_spinner_dropdown_item);

        AQuery aq = new AQuery(view);
        aq.id(R.id.buttonList).clicked(this);
        aq.id(R.id.spinner).adapter(adapter).itemSelected(this);

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
        Location location = Util.getMyLocation();
        if (location != null) {
            return new LatLng(location.getLatitude(), location.getLongitude());
        }
        return null;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Projection projection = mMap.getProjection();
        VisibleRegion visibleRegion = projection.getVisibleRegion();
        mSearchNearLeftLatLng = visibleRegion.nearLeft;
        mSearchFarRightLatLng = visibleRegion.farRight;
        searchItems();
    }


    private void updateMapMarkers(Collection<IGeoPoint> items) {
        if (items == null) {
            mListingMap.clear();
            mMap.clear();
        }

        // Ignore existing items
        List<IGeoPoint> ignore = Lists.newArrayList();
        for (IGeoPoint l : items) {
            if (mListingMap.containsValue(l)) {
                ignore.add(l);
            }
        }
        items.removeAll(ignore);

        // Remove unneeded items
        List<Marker> unneeded = Lists.newArrayList();
        for (Map.Entry<Marker, IGeoPoint> entry : mListingMap.entrySet()) {
            if (!items.contains(entry.getValue()) && !ignore.contains(entry.getValue())) {
                unneeded.add(entry.getKey());
                entry.getKey().remove();
            }
        }
        for (Marker m : unneeded) {
            mListingMap.remove(m);
        }


        // Adds news items as Marker
        for (IGeoPoint l : items) {
            LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
            Marker m = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .position(latLng));
            mListingMap.put(m, l);
        }


    }

    private void searchItems() {
        if (mSearchFarRightLatLng == null || mSearchNearLeftLatLng == null) return;

        Client.RestListener<BaseResult> mListener = new Client.RestListener<BaseResult>() {
            @Override
            public void onComplete(BaseResult result) {
                displayProgress(false);
                List listings = result.getResults();
                if (listings != null) {
                    Collection<IGeoPoint> items = Collections2.transform(listings, new Function<Object, IGeoPoint>() {
                        @Override
                        public IGeoPoint apply(Object geoPoint) {
                            return (IGeoPoint) geoPoint;
                        }
                    });
                    updateMapMarkers(items);
                }
            }

            @Override
            public void onFail(Exception ex) {
                displayProgress(false);
                ex.printStackTrace();
                Toast.makeText(mBaseActivity, "Fail: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        displayProgress(true);
        new Client.Builder(mClass)
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
        IGeoPoint item = mListingMap.get(marker);
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
                //intent.putExtra(ListingResultActivity.EXTRA_ITEMS, items);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {

            case 0:
                mClass = ListingResult.class;
                break;

            case 1:
                mClass = DealResult.class;
                break;

            case 2:
                mClass = ClassifiedResult.class;
                break;

            case 3:
                mClass = EventResult.class;
                break;
        }
        searchItems();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void displayProgress(boolean display) {
        if (getActivity() != null) {
            getActivity().setProgressBarIndeterminateVisibility(display);
        }
    }
}


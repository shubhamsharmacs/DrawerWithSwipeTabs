package com.arcasolutions.ui.fragment.map;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.implementation.IGeoPoint;
import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.Classified;
import com.arcasolutions.api.model.Deal;
import com.arcasolutions.api.model.Event;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.OnModuleSelectionListener;
import com.arcasolutions.ui.activity.BaseActivity;
import com.arcasolutions.util.FmtUtil;
import com.arcasolutions.util.LocationUtil;
import com.arcasolutions.util.Util;
import com.arcasolutions.view.DrawView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.weedfinder.R;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MyMapFragment extends Fragment implements
        GoogleMap.OnCameraChangeListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        DrawView.OnDrawListener {

    public interface OnShowAsListListener {
        void onShowAsList(ArrayList<Module> result, Class<? extends BaseResult> clazz);
    }

    public interface OnSearchFilterListener {
        void onSearchFilter();
    }

    private BaseActivity mBaseActivity;
    private GoogleMap mMap;

    private LatLng mSearchNearLeftLatLng;
    private LatLng mSearchFarRightLatLng;
    private Filter mFilter = new Filter();

    private Class<? extends BaseResult> mClass;

    private final Map<Marker, IGeoPoint> mModuleMap = Maps.newHashMap();

    private DrawView mDrawView;
    private Polygon mMapPolygon;
    private GroundOverlay mGroundOverlay;
    private com.arcasolutions.graphics.Polygon mPolygon;
    private LatLng mNearLeft;
    private LatLng mFarRight;
    private OnShowAsListListener mListener;
    private OnModuleSelectionListener mSelectionListener;
    private OnSearchFilterListener mSearchFilterListener;

    private final Map<String, Bitmap> mUrlBitmapMap = Maps.newHashMap();

    public MyMapFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnShowAsListListener) {
            mListener = (OnShowAsListListener) activity;
        }
        if (activity instanceof OnModuleSelectionListener) {
            mSelectionListener = (OnModuleSelectionListener) activity;
        }
        if (activity instanceof OnSearchFilterListener) {
            mSearchFilterListener = (OnSearchFilterListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mBaseActivity = (BaseActivity) getActivity();
        mClass = Filter.getModuleClass(Filter.ModuleIndex.LISTING);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mDrawView = (DrawView) rootView.findViewById(R.id.drawer);
        mDrawView.setOnDrawListener(this);

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
        aq.id(R.id.buttonDraw).clicked(this);
        aq.id(R.id.action_filter_search).clicked(this);
       // aq.id(R.id.spinner).adapter(adapter).itemSelected(this);

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
        if (mPolygon != null) return;
        searchItems();
    }

    private void removeMarker(Object... markers) {
        if (markers != null) {
            for (Object object : markers) {
                AQuery aq = new AQuery(getView());
                Object tag = aq.id(R.id.mapInfoView).getTag();
                if (object.equals(tag)) {
                    aq.gone();
                }
                mModuleMap.remove(object);
                if (object instanceof Marker)
                    ((Marker)object).remove();
            }
        }
    }

    private void updateMapMarkers(final Collection<IGeoPoint> items) {
        if (items == null || items.isEmpty()) {
            //mModuleMap.clear();
            removeMarker(mModuleMap.keySet().toArray());
            mMap.clear();
            return;
        }

        Projection projection = mMap.getProjection();
        // Descarta itens localizados fora do poligono
        if (mPolygon != null) {
            final List<IGeoPoint> discart = Lists.newArrayList();
            for (IGeoPoint p : items) {
                if (!mPolygon.inside(projection.toScreenLocation(new LatLng(p.getLatitude(), p.getLongitude())))) {
                    discart.add(p);
                }
            }
            items.removeAll(discart);
        }

        // Ignore existing items
        List<IGeoPoint> ignore = Lists.newArrayList();
        for (IGeoPoint l : items) {
            if (mModuleMap.containsValue(l)) {
                ignore.add(l);
            }
        }
        items.removeAll(ignore);

        // Remove unneeded items
        List<Marker> unneeded = Lists.newArrayList();
        for (Map.Entry<Marker, IGeoPoint> entry : mModuleMap.entrySet()) {
            if ((!items.contains(entry.getValue()) && !ignore.contains(entry.getValue()))
                    // Itens fora do poligono
                    || (mPolygon != null && !mPolygon.inside(projection.toScreenLocation(entry.getKey().getPosition())))) {
                unneeded.add(entry.getKey());
                entry.getKey().remove();
            }
        }
        removeMarker(unneeded.toArray());
//        for (Marker m : unneeded) {
//            mModuleMap.remove(m);
//        }



        // Adds news items as Marker
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                for (IGeoPoint p : items) {
                    String imageUrl = p.getImageUrlCat();
                    if (!mUrlBitmapMap.containsKey(imageUrl)) {
                        try {
                            InputStream inputStream = new URL(imageUrl).openStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 40, 40, true);
                            mUrlBitmapMap.put(imageUrl, scaledBitmap);
                        } catch (Exception e) {
                            mUrlBitmapMap.put(imageUrl, BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_marker));
                        }
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                for (IGeoPoint l : items) {
                    LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(mUrlBitmapMap.get(l.getImageUrlCat())))
                            .position(latLng));
                    mModuleMap.put(m, l);
                }
            }
        }.execute();


    }

    private void searchItems() {
        if (mSearchFarRightLatLng == null || mSearchNearLeftLatLng == null) return;

        double nearLeftLat = mSearchNearLeftLatLng.latitude;
        double nearLeftLng = mSearchNearLeftLatLng.longitude;
        double farRightLat = mSearchFarRightLatLng.latitude;
        double farRightLng = mSearchFarRightLatLng.longitude;

        // Se existing desenho no mapa, utilizar a área desenhada.
        if (mPolygon != null) {
            nearLeftLat = mNearLeft.latitude;
            nearLeftLng = mNearLeft.longitude;
            farRightLat = mFarRight.latitude;
            farRightLng = mFarRight.longitude;
        }

        Client.RestListener<BaseResult> mListener = new Client.RestListener<BaseResult>() {
            @Override
            public void onComplete(BaseResult result) {
                displayProgress(false);
                List listings = result.getResults();
                Collection<IGeoPoint> items = null;
                if (listings != null) {
                    items = Collections2.transform(listings, new Function<Object, IGeoPoint>() {
                        @Override
                        public IGeoPoint apply(Object geoPoint) {
                            return (IGeoPoint) geoPoint;
                        }
                    });
                }
                updateMapMarkers(items);
            }

            @Override
            public void onFail(Exception ex) {
                displayProgress(false);
                ex.printStackTrace();
                Toast.makeText(mBaseActivity, "Fail: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        displayProgress(true);
        Client.Builder builder = new Client.Builder(mClass);
        builder.searchBy(SearchBy.MAP)
                .region(
                        nearLeftLat,
                        nearLeftLng,
                        farRightLat,
                        farRightLng);

        if (mFilter != null) {
            if (!TextUtils.isEmpty(mFilter.getKeyword())) {
                builder.keyword(mFilter.getKeyword());
            }


            if (mFilter.getCategory() != null && !TextUtils.isEmpty((mFilter.getCategory()))) {
                builder.category(mFilter.getCategory());
            }

            builder.ratings(mFilter.getRatings());

        }

        builder.execAsync(mListener);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final IGeoPoint item = mModuleMap.get(marker);
        if (item != null) {
            AQuery aq = new AQuery(getView());
            aq.id(R.id.mapInfoView).gone();
            aq.id(R.id.mapInfoTitle).visible().text(item.getTitle());
            aq.id(R.id.mapInfoRatingBar).visible().rating(item.getRating());
            aq.id(R.id.mapInfoDistance).visible().text(FmtUtil.distance(item.getLatitude(), item.getLongitude()));
            aq.id(R.id.mapInfoAddress).visible().text(item.getAddress());

            if (item instanceof Deal) {
                aq.id(R.id.mapInfoDistance).invisible();
                aq.id(R.id.mapInfoAddress).invisible();
            } else if (item instanceof Classified || item instanceof Event) {
                aq.id(R.id.mapInfoRatingBar).invisible();
            }

            aq.id(R.id.mapInfoView).tag(marker).clicked(this, "onClick").visible().animate(android.R.anim.fade_in);
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

            case R.id.action_filter_search:
                mSearchFilterListener.onSearchFilter();
                break;

            case R.id.mapInfoView:
                Object tag = view.getTag();
                if (tag instanceof Marker) {
                    IGeoPoint point = mModuleMap.get(tag);
                    mSelectionListener.onModuleSelected((Module) point, 0, 0);
                }
                break;

            case R.id.buttonList:
                final Collection<IGeoPoint> result = mModuleMap.values();
                if (mListener != null && result != null) {
                    List<IGeoPoint> items = Lists.newArrayList(result);
                    // Ordena por distancia
                    Collections.sort(items, new Comparator<IGeoPoint>() {
                        @Override
                        public int compare(IGeoPoint p1, IGeoPoint p2) {
                            double d1 = Util.distanceFromMe(p1.getLatitude(), p1.getLongitude());
                            double d2 = Util.distanceFromMe(p2.getLatitude(), p2.getLongitude());

                            return d1 < d2 ? -1 : (d1 > d2 ? 1 : 0);
                        }
                    });
                    // Converte para module
                    Collection<Module> modules = Collections2.transform(items, new Function<IGeoPoint, Module>() {
                        @Override
                        public Module apply(IGeoPoint point) {
                            return (Module) point;
                        }
                    });

                    mListener.onShowAsList(Lists.newArrayList(modules), mClass);
                }

                break;

            case R.id.buttonDraw:
                onDrawClickButton();
                break;


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mClass = Filter.getModuleClass(i);
        mFilter.setModuleIndex(i);
        searchItems();
    }

    public Filter getFilter() {
        return mFilter;
    }

    public void setFilter(Filter filter) {
        if (mFilter == null) return;

        mFilter = filter;
        mClass = Filter.getModuleClass(mFilter.getModuleIndex());

        AQuery aq = new AQuery(getView());
       // aq.id(R.id.spinner).setSelection(mFilter.getModuleIndex());

        if (!TextUtils.isEmpty(mFilter.getLocation())) {
            LocationUtil.geocoder(getActivity(), mFilter.getLocation(), new LocationUtil.GeocoderCallback() {
                @Override
                public void onLatLng(LatLng point) {
                    if (point != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
                    } else {
                        Toast.makeText(getActivity(), "Location not found.", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        } else {
            searchItems();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void displayProgress(boolean display) {
        if (getActivity() != null) {
            getActivity().setProgressBarIndeterminateVisibility(display);
        }
    }

    public void onDrawClickButton() {
        boolean isMapDrawn = false;
        if (mPolygon != null) {
            mPolygon = null;
        }

        if (mMapPolygon != null) {
            mMapPolygon.remove();
            mMapPolygon = null;
            isMapDrawn = true;
        }

        if (mGroundOverlay != null) {
            mGroundOverlay.remove();
            mGroundOverlay = null;
            isMapDrawn = true;
        }

        boolean isDrawViewVisible = false;
        if (mDrawView.getVisibility() != View.GONE) {
            mDrawView.setVisibility(View.GONE);
            isDrawViewVisible = true;
        }

        // Remove o desenha e refaz a pesquisa
        if (isMapDrawn || isDrawViewVisible) {
            searchItems();
            new AQuery(getView())
                    .id(R.id.buttonDraw)
                    .image(R.drawable.ic_action_content_edit);
            return;
        }

        mDrawView.setVisibility(View.VISIBLE);
        AQuery aq = new AQuery(getView());
        aq.id(R.id.mapInfoView).gone();
        aq.id(R.id.buttonDraw)
            .image(R.drawable.ic_action_content_edit)
            .getView().setPressed(true);

    }

    @Override
    public void onDrawDone(List<Point> points, Bitmap ignored) {
        if (points == null || ignored == null || points.isEmpty()) return;

        Resources res = getResources();
        int fillColor = res.getColor(R.color.map_draw_fill);
        int strokeColor = res.getColor(R.color.map_draw_stroke);

        int width = mDrawView.getWidth();
        int height = mDrawView.getHeight();

        Projection projection = mMap.getProjection();

        List<LatLng> latLngs = Lists.newArrayList();
        mPolygon = new com.arcasolutions.graphics.Polygon(points.size());

        for (Point point : points) {
            mPolygon.addPoint(point.x, point.y);
            latLngs.add(projection.fromScreenLocation(point));
        }
        mPolygon.close();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(fillColor);

        new Canvas(bitmap).drawPath(mPolygon, paint);

        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                .positionFromBounds(visibleRegion.latLngBounds);

        mGroundOverlay = mMap.addGroundOverlay(newarkMap);
        mMapPolygon = mMap.addPolygon(new PolygonOptions()
                .addAll(latLngs)
                .strokeWidth(3)
                .strokeColor(strokeColor));

        mDrawView.setVisibility(View.GONE);
        mDrawView.clear();

        RectF bounds = mPolygon.getBounds();
        mNearLeft = projection.fromScreenLocation(new Point((int)bounds.left,  (int)bounds.bottom));
        mFarRight = projection.fromScreenLocation(new Point((int)bounds.right, (int)bounds.top));

        new AQuery(getView())
                .id(R.id.buttonDraw)
                .image(R.drawable.ic_action_content_discard)
                .getView().setPressed(false);

        searchItems();
    }
}


package com.arcasolutions.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.arcasolutions.R;
import com.arcasolutions.api.model.ArticleCategoryResult;
import com.arcasolutions.api.model.BaseCategoryResult;
import com.arcasolutions.api.model.ClassifiedCategoryResult;
import com.arcasolutions.api.model.DealCategoryResult;
import com.arcasolutions.api.model.EventCategoryResult;
import com.arcasolutions.api.model.ListingCategoryResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseActivity extends ActionBarActivity implements
           LocationClient.ConnectionCallbacks, LocationClient.OnConnectionFailedListener, AdapterView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationClient = new LocationClient(this, this, this);
        mLocationClient.connect();


        List<NavItem> options = Lists.newArrayList();
        options.add(new NavItem(R.drawable.ic_location_map, R.string.drawerMap, MapActivity.class, null));
        options.add(new NavItem(R.drawable.ic_collections_go_to_today, R.string.drawerBusiness, CategoryResultActivity.class, buildCatExtras(ListingCategoryResult.class)));
        options.add(new NavItem(R.drawable.ic_collections_go_to_today, R.string.drawerDeals, CategoryResultActivity.class, buildCatExtras(DealCategoryResult.class)));
        options.add(new NavItem(R.drawable.ic_collections_go_to_today, R.string.drawerClassifieds, CategoryResultActivity.class, buildCatExtras(ClassifiedCategoryResult.class)));
        options.add(new NavItem(R.drawable.ic_collections_go_to_today, R.string.drawerEvents, CategoryResultActivity.class, buildCatExtras(EventCategoryResult.class)));
        options.add(new NavItem(R.drawable.ic_collections_go_to_today, R.string.drawerArticles, CategoryResultActivity.class, buildCatExtras(ArticleCategoryResult.class)));
        options.add(new NavItem(R.drawable.ic_collections_go_to_today, R.string.drawerMyFavorites, CategoryResultActivity.class, null));
        options.add(new NavItem(R.drawable.ic_action_settings, R.string.drawerSetting, CategoryResultActivity.class, null));


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.setAdapter(new NavigationAdapter(this, options));
        mDrawerList.setOnItemClickListener(this);



    }

    private Bundle buildCatExtras(Class<? extends BaseCategoryResult> clazz) {
        final Bundle extras = new Bundle();
        extras.putSerializable(CategoryResultActivity.EXTRA_TYPE, clazz);
        return extras;
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public LocationClient getLocationClient() {
        return mLocationClient;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        NavItem navItem = (NavItem) adapterView.getItemAtPosition(i);
        Intent intent = new Intent(this, navItem.clazz);
        if (navItem.extras != null)
            intent.putExtras(navItem.extras);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private class NavigationAdapter extends ArrayAdapter<NavItem> {

        public NavigationAdapter(Context context, List<NavItem> objects) {
            super(context, R.layout.simple_list_item_navigation_drawer, android.R.id.text1, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            NavItem item = getItem(position);
            ((ImageView) view.findViewById(android.R.id.icon)).setImageResource(item.drawableId);
            ((TextView) view.findViewById(android.R.id.text1)).setText(item.stringId);

            return view;
        }
    }

    class NavItem {

        NavItem(int _drawableId, int _stringId, Class<? extends ActionBarActivity> _clazz, Bundle _extras) {
            drawableId = _drawableId;
            stringId = _stringId;
            clazz = _clazz;
            extras = _extras;
        }

        int drawableId;
        int stringId;
        Class<? extends ActionBarActivity> clazz;
        Bundle extras;
    }
    
}

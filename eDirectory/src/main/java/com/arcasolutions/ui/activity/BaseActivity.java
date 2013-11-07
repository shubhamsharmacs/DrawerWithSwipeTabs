package com.arcasolutions.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.arcasolutions.ui.fragment.SettingFragment;
import com.arcasolutions.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.common.collect.Lists;

import java.util.List;

public abstract class BaseActivity extends ActionBarActivity implements
        LocationListener,
        LocationClient.ConnectionCallbacks,
        LocationClient.OnConnectionFailedListener, AdapterView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private String APPTAG;

    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    // Name of shared preferences repository that stores persistent state
    public static final String SHARED_PREFERENCES =
            "LOCATION_SHARED_PREFERENCES";

    // Key for storing the "updates requested" flag in shared preferences
    public static final String KEY_UPDATES_REQUESTED =
            "LOCATION_KEY_UPDATES_REQUESTED";

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    public static final int MILLISECONDS_PER_SECOND = 1000;

    // The update interval
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

    // A fast interval ceiling
    public static final int FAST_CEILING_IN_SECONDS = 1;

    // Update interval in milliseconds
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;

    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;

    /*
     * Note if updates have been turned on. Starts out as "false"; is set to "true" in the
     * method handleRequestSuccess of LocationUpdateReceiver.
     *
     */
    boolean mUpdatesRequested = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);

        APPTAG = getString(R.string.app_name);

        // Create a new global location parameters object
        mLocationRequest = LocationRequest.create();

        /*
         * Set the update interval
         */
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        // Note that location updates are off until the user turns them on
        mUpdatesRequested = false;

        // Open Shared Preferences
        mPrefs = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        // Get an editor
        mEditor = mPrefs.edit();

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);


        List<NavItem> options = Lists.newArrayList();
        options.add(new NavItem(R.drawable.ic_map, R.string.drawerMap, MapActivity.class, null));
        options.add(new NavItem(R.drawable.ic_business, R.string.drawerBusiness, CategoryResultActivity.class, buildCatExtras(ListingCategoryResult.class)));
        options.add(new NavItem(R.drawable.ic_tag, R.string.drawerDeals, CategoryResultActivity.class, buildCatExtras(DealCategoryResult.class)));
        options.add(new NavItem(R.drawable.ic_classifieds, R.string.drawerClassifieds, CategoryResultActivity.class, buildCatExtras(ClassifiedCategoryResult.class)));
        options.add(new NavItem(R.drawable.ic_events, R.string.drawerEvents, CategoryResultActivity.class, buildCatExtras(EventCategoryResult.class)));
        options.add(new NavItem(R.drawable.ic_articles, R.string.drawerArticles, CategoryResultActivity.class, buildCatExtras(ArticleCategoryResult.class)));
        options.add(new NavItem(R.drawable.ic_favorites, R.string.drawerMyFavorites, CategoryResultActivity.class, null));
        options.add(new NavItem(R.drawable.ic_settings, R.string.drawerSetting, SettingActivity.class, null));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.setAdapter(new NavigationAdapter(this, options));
        mDrawerList.setOnItemClickListener(this);
    }

    /*
     * Called when the Activity is no longer visible at all.
     * Stop updates and disconnect.
     */
    @Override
    public void onStop() {

        // If the client is connected
        if (mLocationClient.isConnected()) {
            stopPeriodicUpdates();
        }

        // After disconnect() is called, the client is considered "dead".
        mLocationClient.disconnect();

        super.onStop();
    }
    /*
     * Called when the Activity is going into the background.
     * Parts of the UI may be visible, but the Activity is inactive.
     */
    @Override
    public void onPause() {

        // Save the current setting for updates
        mEditor.putBoolean(KEY_UPDATES_REQUESTED, mUpdatesRequested);
        mEditor.commit();

        super.onPause();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {

        super.onStart();

        /*
         * Connect the client. Don't re-start any requests here;
         * instead, wait for onResume()
         */
        mLocationClient.connect();

    }
    /*
     * Called when the system detects that this Activity is now visible.
     */
    @Override
    public void onResume() {
        super.onResume();

        // If the app already has a setting for getting location updates, get it
        if (mPrefs.contains(KEY_UPDATES_REQUESTED)) {
            mUpdatesRequested = mPrefs.getBoolean(KEY_UPDATES_REQUESTED, false);

            // Otherwise, turn off location updates until requested
        } else {
            mEditor.putBoolean(KEY_UPDATES_REQUESTED, false);
            mEditor.commit();
        }

    }

    /*
     * Handle results returned to this Activity by other Activities started with
     * startActivityForResult(). In particular, the method onConnectionFailed() in
     * LocationUpdateRemover and LocationUpdateRequester may call startResolutionForResult() to
     * start an Activity that handles Google Play services problems. The result of this
     * call returns here, to onActivityResult.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case RESULT_OK:

                        break;

                    // If any other result was returned by Google Play services
                    default:

                        break;
                }

                // If any other request code was received
            default:
                // Report that this Activity received an unknown requestCode

                break;
        }
    }

    private Bundle buildCatExtras(Class<? extends BaseCategoryResult> clazz) {
        final Bundle extras = new Bundle();
        extras.putSerializable(CategoryResultActivity.EXTRA_TYPE, clazz);
        return extras;
    }


    @Override
    public void onConnected(Bundle bundle) {
        Util.setMyLocation(mLocationClient.getLastLocation());

        if (mUpdatesRequested) {
            startPeriodicUpdates();
        }
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {

                // Log the error
                e.printStackTrace();
            }
        } else {

            // If no resolution is available, display a dialog to the user with the error.
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Util.setMyLocation(location);
    }

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
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

    /**
     * Show a dialog returned by Google Play services for the
     * connection error code
     *
     * @param errorCode An error code returned from onConnectionFailed
     */
    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getSupportFragmentManager(), APPTAG);
        }
    }

    /**
     * Define a DialogFragment to display the error dialog generated in
     * showErrorDialog.
     */
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /**
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
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

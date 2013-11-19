package com.arcasolutions.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.arcasolutions.R;
import com.arcasolutions.ui.fragment.map.Filter;
import com.arcasolutions.ui.fragment.map.MyMapFilterFragment;
import com.arcasolutions.ui.fragment.map.MyMapFragment;

public class MapActivity extends BaseActivity {

    private MyMapFragment mMyMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMyMapFragment = (MyMapFragment) getSupportFragmentManager().findFragmentByTag("map");
        if (mMyMapFragment == null) {
            mMyMapFragment = new MyMapFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_content, mMyMapFragment, "map")
                    .commit();
        }
    }

    private void dismissFilter() {
        Fragment filterFragment = getSupportFragmentManager()
                .findFragmentByTag("filter");
        if (filterFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(filterFragment)
                    .commit();
        }

        supportInvalidateOptionsMenu();
    }

    private void showFilter() {
        Fragment filterFragment = getSupportFragmentManager().findFragmentByTag("filter");
        if (filterFragment == null) {
            filterFragment = MyMapFilterFragment.newInstance(mMyMapFragment.getFilter().getModuleIndex());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_content, filterFragment, "filter")
                    .commit();
        }

        supportInvalidateOptionsMenu();
    }

    public void onSearch(View view) {
        MyMapFilterFragment filterFragment = (MyMapFilterFragment) getSupportFragmentManager().findFragmentByTag("filter");
        if (filterFragment != null) {
            Filter filter = filterFragment.getFilter();
            dismissFilter();
            mMyMapFragment.setFilter(filter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                showFilter();
                return true;

            case R.id.action_cancel:
                dismissFilter();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

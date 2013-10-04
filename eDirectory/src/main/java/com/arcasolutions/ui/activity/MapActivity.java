package com.arcasolutions.ui.activity;

import android.os.Bundle;

import com.arcasolutions.R;
import com.arcasolutions.ui.fragment.MyMapFragment;

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


}

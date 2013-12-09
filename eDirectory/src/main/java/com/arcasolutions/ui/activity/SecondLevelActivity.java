package com.arcasolutions.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.arcasolutions.util.BannerHelper;

public abstract class SecondLevelActivity extends ActionBarActivity {

    private BannerHelper mBannerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBannerHelper = new BannerHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBannerHelper.onStart();
    }

    @Override
    protected void onStop() {
        mBannerHelper.onStop();
        super.onStop();
    }
}

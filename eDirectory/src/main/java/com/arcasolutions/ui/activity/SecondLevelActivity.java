package com.arcasolutions.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.arcasolutions.util.BannerHelper;
import com.weedfinder.R;

public abstract class SecondLevelActivity extends ActionBarActivity {

    private BannerHelper mBannerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBannerHelper = new BannerHelper(this);

        //getSupportActionBar().setIcon(R.drawable.weedfinder);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        getSupportActionBar().setIcon(android.R.color.transparent);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.activity_test, null);

        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        getSupportActionBar().setCustomView(v, layout);

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

package com.arcasolutions.ui.activity;

import android.os.Bundle;

import com.weedfinder.R;
import com.arcasolutions.ui.fragment.SettingFragment;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingFragment mSettingFragment = (SettingFragment) getSupportFragmentManager().findFragmentByTag("setting");
        if (mSettingFragment == null) {
            mSettingFragment = new SettingFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_content, mSettingFragment, "setting")
                    .commit();
        }
    }
}

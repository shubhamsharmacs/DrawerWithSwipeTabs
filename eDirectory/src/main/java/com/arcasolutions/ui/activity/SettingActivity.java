package com.arcasolutions.ui.activity;

import android.os.Bundle;

import com.arcasolutions.R;
import com.arcasolutions.ui.fragment.SettingFragment;

public class SettingActivity extends BaseActivity {

    private SettingFragment mSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettingFragment = (SettingFragment) getSupportFragmentManager().findFragmentByTag("setting");
        if (mSettingFragment == null) {
            mSettingFragment = new SettingFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_content, mSettingFragment, "setting")
                    .commit();
        }
    }
}

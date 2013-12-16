package com.arcasolutions.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.arcasolutions.util.Util;

public class StartUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent;
        if (Util.isNonLocationApp(this)) {
            intent = new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, MapActivity.class);
        }
        startActivity(intent);
        finish();

    }
}

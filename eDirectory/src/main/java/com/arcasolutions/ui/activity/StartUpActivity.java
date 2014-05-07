package com.arcasolutions.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.weedfinder.R;
import com.arcasolutions.util.Util;

public class StartUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        Util.verifyIntenetConnection(this, new Util.ConnectionCallback() {
            @Override
            public void onConnection(boolean hasInternet) {
               /* if (hasInternet) {
                    openApp();
                } else {
                    alertNoInternet();
                }*/
                openApp();
            }
        });

    }

    private void openApp() {
        Intent intent;
        if (Util.isNonLocationApp(this)) {
            intent = new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, MapActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void alertNoInternet() {
        new AlertDialog.Builder(this)
            .setMessage(getString(R.string.no_internet_connection))
            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    finish();
                }
            })
            .setCancelable(true)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            })
            .create().show();
    }
}

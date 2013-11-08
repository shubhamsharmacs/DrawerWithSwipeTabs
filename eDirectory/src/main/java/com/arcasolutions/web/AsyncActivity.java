package com.arcasolutions.web;

import com.arcasolutions.App;

public interface AsyncActivity {

    public App getApplicationContext();

    public void showLoadingProgressDialog();

    public void showProgressDialog(CharSequence message);

    public void dismissProgressDialog();

}

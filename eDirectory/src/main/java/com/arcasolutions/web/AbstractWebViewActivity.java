package com.arcasolutions.web;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.arcasolutions.App;
import com.weedfinder.R;
import com.arcasolutions.util.DialogHelper;

public class AbstractWebViewActivity extends ActionBarActivity implements AsyncActivity {

    private WebView mWebView;

    private ProgressDialog mProgressDialog = null;

    private Activity mActivity;

    @Override
    public App getApplicationContext() {
        return (App) super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        mWebView = new WebView(this);
        setContentView(mWebView);
        mActivity = this;

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mActivity.setTitle(getString(R.string.loading));
                mActivity.setProgress(newProgress * 100);
                if (newProgress == 100) {
                    mActivity.setTitle(R.string.app_name);
                }
            }
        });
    }

    protected WebView getWebView() {
        return mWebView;
    }

    @Override
    public void showLoadingProgressDialog() {
        showProgressDialog(getString(R.string.loading_please_wait));
    }

    @Override
    public void showProgressDialog(CharSequence message) {
        mProgressDialog = DialogHelper.from(this)
                    .progress(message);
    }

    @Override
    public void dismissProgressDialog() {
        boolean destroyed = false;
        if (mProgressDialog != null && !destroyed) {
            mProgressDialog.dismiss();
        }
    }
}

package com.arcasolutions.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.weedfinder.R;

/**
 * Created by willian.feltri on 5/5/14.
 */
public class MenuWebviewActivity extends SecondLevelActivity{

    public static final String EXTRA_URL = "extra_url";

    private WebView webView;
    private String url;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_webview_listing_detail);

        Intent intent = getIntent();
        url = intent.getStringExtra(EXTRA_URL);

        webView = (WebView) findViewById(R.id.menu_webview);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new Browser());

        webView.loadUrl(url);

    }

    private class Browser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

package com.arcasolutions.util;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.Ad;
import com.arcasolutions.api.model.AdResult;
import com.arcasolutions.ui.activity.AboutActivity;
import com.arcasolutions.ui.activity.LoginActivity;
import com.arcasolutions.ui.activity.SettingActivity;
import com.arcasolutions.ui.activity.SignUpActivity;
import com.arcasolutions.ui.activity.event.EventActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class BannerHelper implements Client.RestListener<AdResult> {

    private final Activity mActivity;
    private static final Stack<Ad> mAds = new Stack<Ad>();
    private LinearLayout mBannerPlace;
    private LinearLayout mBannerFrame;


    private static final Class<?>[] ACTIVITIES_TO_IGNORE = new Class<?>[] {
            EventActivity.class, AboutActivity.class, LoginActivity.class,
            SignUpActivity.class, SettingActivity.class
    };

    public BannerHelper(Activity activity) {
        mActivity = activity;
    }

    public void onStart() {
        if (shouldBeIgnored()) return;

        int color = Color.GRAY;
        ViewGroup view = (ViewGroup) mActivity.findViewById(R.id.bannerPlace);
        if (view != null) {
            mBannerPlace = (LinearLayout) view;
            mBannerPlace.setGravity(Gravity.CENTER);
            mBannerPlace.setBackgroundColor(color);
        } else {
            mBannerPlace = new LinearLayout(mActivity);
            mBannerPlace.setGravity(Gravity.CENTER);
            mBannerPlace.setBackgroundColor(color);

            Window w = mActivity.getWindow();
            view = (ViewGroup) w.findViewById(android.R.id.content).getParent();
            view.addView(mBannerPlace);
        }

        showBanner();
    }

    public void onStop() {
        if (shouldBeIgnored()) return;

        removeBanner();
    }

    public void onOpenUrl(View view) {
        Ad ad = (Ad) view.getTag();
        IntentUtil.website(mActivity, ad.getUrl());
    }

    public void onCloseBanner(View view) {
        removeBanner();
    }

    private void showBanner() {
        if (!mAds.isEmpty()) {
            Ad ad = mAds.pop();
            ViewGroup bannerView = getBannerView();
            AQuery aq = new AQuery(bannerView);
            aq.id(android.R.id.icon1).tag(ad).image(ad.getImageUrl()).clicked(this, "onOpenUrl");
            aq.id(android.R.id.icon2).image(R.drawable.banner_closer).clicked(this, "onCloseBanner");
            removeBanner();
            mBannerPlace.addView(bannerView);
        } else {
            new Client.Builder(AdResult.class)
                    .execAsync(this);
        }
    }

    private void removeBanner() {
        ViewGroup bannerView = getBannerView();
        if (bannerView == null) return;

        ViewGroup bannerParent = (ViewGroup) bannerView.getParent();
        if (bannerParent != null) bannerParent.removeView(bannerView);
    }

    private ViewGroup getBannerView() {
        if (mBannerFrame == null) {

            mBannerFrame = (LinearLayout) mActivity.findViewById(R.id.bannerFrame);
            if (mBannerFrame == null) {
                mBannerFrame = new LinearLayout(mActivity);
                mBannerFrame.setId(R.id.bannerFrame);
            }

            ImageView bannerImage = new ImageView(mActivity);
            bannerImage.setId(android.R.id.icon1);
            bannerImage.setAdjustViewBounds(true);
            bannerImage.setScaleType(ImageView.ScaleType.FIT_XY);

            ImageView closerImage = new ImageView(mActivity);
            closerImage.setId(android.R.id.icon2);
            closerImage.setAdjustViewBounds(true);
            closerImage.setImageResource(R.drawable.banner_closer);

            mBannerFrame.setOrientation(LinearLayout.HORIZONTAL);
            mBannerFrame.addView(bannerImage, 290, 50);
            mBannerFrame.addView(closerImage);
        }

        return mBannerFrame;
    }

    private boolean shouldBeIgnored() {
        return Arrays.asList(ACTIVITIES_TO_IGNORE)
                .contains(mActivity.getClass());
    }

    @Override
    public void onComplete(AdResult result) {
        List<Ad> ads = result.getResults();
        if (ads != null && !ads.isEmpty()) {
            mAds.addAll(ads);
            showBanner();
        }
    }

    @Override
    public void onFail(Exception ex) {

    }
}

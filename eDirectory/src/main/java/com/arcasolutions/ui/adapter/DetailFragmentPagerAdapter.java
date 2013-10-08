package com.arcasolutions.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.common.collect.Lists;

import java.util.List;

public class DetailFragmentPagerAdapter extends FragmentStatePagerAdapter {

    class PagerInfo {
        PagerInfo(CharSequence title, Class clss, Bundle args) {
            this.title = title;
            this.clss = clss;
            this.args = args;
        }
        CharSequence title;
        Class clss;
        Bundle args;
    }

    private final Context mContext;
    private final List<PagerInfo> mPages = Lists.newArrayList();

    public DetailFragmentPagerAdapter(FragmentActivity activity) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
    }

    public void add(CharSequence title, Class clss, Bundle args) {
        mPages.add(new PagerInfo(title, clss, args));
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {
        PagerInfo info = mPages.get(i);
        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPages.get(position).title;
    }
}

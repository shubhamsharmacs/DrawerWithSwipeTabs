package com.arcasolutions.ui.activity.event;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.arcasolutions.R;
import com.arcasolutions.api.model.EventCategoryResult;
import com.arcasolutions.ui.activity.BaseActivity;
import com.arcasolutions.ui.adapter.EventCalendarAdapter;
import com.arcasolutions.ui.fragment.CategoryResultFragment;
import com.arcasolutions.ui.fragment.event.EventCalendarFragment;

import java.util.ArrayList;

public class EventActivity extends BaseActivity
        implements EventCalendarFragment.OnDateChangeListener {

    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.pager);
        setContentView(mViewPager);

        final ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mTabsAdapter = new TabsAdapter(this, mViewPager);

        CategoryResultFragment categoryFragment = CategoryResultFragment.newInstance(EventCategoryResult.class);
        mTabsAdapter.addTab(bar.newTab().setText("Categories"), categoryFragment.getClass(), categoryFragment.getArguments());

        EventCalendarFragment calendarFragment = EventCalendarFragment.newInstance();
        mTabsAdapter.addTab(bar.newTab().setText("Calendar"), calendarFragment.getClass(), calendarFragment.getArguments());

    }

    @Override
    public void onMonthChanged(int month, int year) {

    }

    @Override
    public void onDateClicked(int day, int month, int year) {

    }

    private static class TabsAdapter extends FragmentPagerAdapter
            implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

        private final Context mContext;
        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(Class<?> _class, Bundle _args) {
                clss = _class;
                args = _args;
            }
        }

        TabsAdapter(FragmentActivity activity, ViewPager viewPager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mActionBar = ((ActionBarActivity) activity).getSupportActionBar();
            mViewPager = viewPager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
            TabInfo info = new TabInfo(clss, args);
            tab.setTag(info);
            tab.setTabListener(this);
            mTabs.add(info);
            mActionBar.addTab(tab);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int i) {
            TabInfo info = mTabs.get(i);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onPageScrolled(int i, float v, int i2) {
        }

        @Override
        public void onPageSelected(int i) {
            mActionBar.setSelectedNavigationItem(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            Object tag = tab.getTag();
            for (int i=0; i<mTabs.size(); i++) {
                if (mTabs.get(i) == tag) {
                    mViewPager.setCurrentItem(i);
                }
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }
}

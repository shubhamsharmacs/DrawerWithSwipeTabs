package com.arcasolutions.ui.activity.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;

import com.arcasolutions.R;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.EventCategory;
import com.arcasolutions.api.model.EventCategoryResult;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.OnModuleSelectionListener;
import com.arcasolutions.ui.activity.BaseActivity;
import com.arcasolutions.ui.activity.CategoryResultActivity;
import com.arcasolutions.ui.activity.listing.ListingResultActivity;
import com.arcasolutions.ui.fragment.CategoryResultFragment;
import com.arcasolutions.ui.fragment.event.EventCalendarFragment;
import com.arcasolutions.ui.fragment.event.EventSectionListFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class EventActivity extends BaseActivity
        implements EventCalendarFragment.OnDateChangeListener,
        CategoryResultFragment.OnCategorySelectionListener,
        OnModuleSelectionListener {

    private ViewPager mViewPager;
    private Calendar mCalendarMonth;

    private final Bundle mListArgs = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.pager);
        ViewGroup rootView = (ViewGroup) findViewById(R.id.frame_content);
        rootView.addView(mViewPager);

        final ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        TabsAdapter mTabsAdapter = new TabsAdapter(this, mViewPager);

        CategoryResultFragment categoryFragment = CategoryResultFragment.newInstance(EventCategoryResult.class);
        mTabsAdapter.addTab(bar.newTab().setText(R.string.event_tab_categories), categoryFragment.getClass(), categoryFragment.getArguments());

        EventCalendarFragment calendarFragment = EventCalendarFragment.newInstance();
        mTabsAdapter.addTab(bar.newTab().setText(R.string.event_tab_calendar), calendarFragment.getClass(), calendarFragment.getArguments());

        mCalendarMonth = Calendar.getInstance();
        mListArgs.putSerializable(EventSectionListFragment.ARG_INITIAL_DATE, mCalendarMonth);
        mTabsAdapter.addTab(bar.newTab().setText(R.string.event_tab_list), EventSectionListFragment.class, mListArgs);

        bar.setSelectedNavigationItem(1);
    }

    @Override
    public void onMonthChanged(int month, int year) {
        mCalendarMonth = Calendar.getInstance();
        mCalendarMonth.set(year, month, 1);
        mListArgs.putSerializable(EventSectionListFragment.ARG_INITIAL_DATE, mCalendarMonth);
    }

    @Override
    public void onDateClicked(int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        mListArgs.putSerializable(EventSectionListFragment.ARG_INITIAL_DATE, cal);
        mViewPager.setCurrentItem(2);
    }

    @Override
    public void onCategorySelected(BaseCategory category) {
        if (category == null) return;

        if (category.getTotalSubs() > 0) {
            Intent intent = new Intent(this, CategoryResultActivity.class);
            intent.putExtra(CategoryResultActivity.EXTRA_TYPE, EventCategoryResult.class);
            intent.putExtra(CategoryResultActivity.EXTRA_CATEGORY, category);
            startActivity(intent);
        } else if (category.getActiveItems() > 0) {
            if (category instanceof EventCategory) {
                Intent intent = new Intent(this, EventResultActivity.class);
                intent.putExtra(ListingResultActivity.EXTRA_CATEGORY, category);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onModuleSelected(Module module, int position, long id) {
        if (module != null) {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra(EventDetailActivity.EXTRA_ID, module.getId());
            startActivity(intent);
        }
    }

    private class TabsAdapter extends FragmentPagerAdapter
            implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

        private final Context mContext;
        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        final class TabInfo {
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
            if (i == 1) {
                mListArgs.putSerializable(EventSectionListFragment.ARG_INITIAL_DATE, mCalendarMonth);
            }
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

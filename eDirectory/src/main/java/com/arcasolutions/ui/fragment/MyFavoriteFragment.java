package com.arcasolutions.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcasolutions.R;
import com.arcasolutions.api.model.Article;
import com.arcasolutions.api.model.Classified;
import com.arcasolutions.api.model.Deal;
import com.arcasolutions.api.model.Event;
import com.arcasolutions.api.model.Listing;
import com.google.android.gms.internal.ac;
import com.google.common.collect.Lists;

import java.util.List;

public class MyFavoriteFragment extends Fragment {

    private static final int[] TITLE_IDS = new int[] {R.string.drawerBusiness, R.string.drawerDeals, R.string.drawerEvents, R.string.drawerClassifieds, R.string.drawerArticles};
    private static final Class[] CLASSES = new Class[]{Listing.class, Deal.class, Event.class, Classified.class, Article.class};
    private FavoriteAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new FavoriteAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_favorite, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(mAdapter);

        PagerTabStrip tabStrip = (PagerTabStrip) view.findViewById(R.id.tabStrip);
        tabStrip.setBackgroundColor(getResources().getColor(R.color.pagerStripBackground));
        tabStrip.setDrawFullUnderline(false);
        tabStrip.setTabIndicatorColor(getResources().getColor(R.color.pagerStripIndicator));

        if (CLASSES != null) {
            for (int i=0; i<CLASSES.length; i++) {
                ModuleFavoriteFragment fragment = ModuleFavoriteFragment.newInstance(CLASSES[i]);
                mAdapter.add(fragment, TITLE_IDS[i]);
            }

            pager.setOffscreenPageLimit(1);
        }

    }

    static class FavoriteAdapter extends FragmentPagerAdapter {

        private final Context mContext;
        private final List<Info> mInfos = Lists.newArrayList();

        final class Info {
            Class<?> clazz;
            Bundle args;
            int titleResId;
            Info(Class<?> _clazz, Bundle _args, int _titleResId) {
                clazz = _clazz;
                args = _args;
                titleResId = _titleResId;
            }
        }

        public FavoriteAdapter(FragmentActivity activity) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
        }

        public void add(Fragment fragment, int titleResId) {
            mInfos.add(new Info(fragment.getClass(), fragment.getArguments(), titleResId));
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int i) {
            Info info = mInfos.get(i);
            return Fragment.instantiate(mContext, info.clazz.getName(), info.args);
        }

        @Override
        public int getCount() {
            return mInfos.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Info info = mInfos.get(position);
            return mContext.getString(info.titleResId);
        }
    }

}

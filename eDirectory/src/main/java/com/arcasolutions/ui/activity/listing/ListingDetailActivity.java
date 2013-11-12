package com.arcasolutions.ui.activity.listing;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.ReviewModule;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.api.model.ListingResult;
import com.arcasolutions.ui.adapter.DetailFragmentPagerAdapter;
import com.arcasolutions.ui.fragment.DescriptionFragment;
import com.arcasolutions.ui.fragment.GalleryFragment;
import com.arcasolutions.ui.fragment.ReviewListFragment;
import com.arcasolutions.ui.fragment.listing.ListingOverviewFragment;

import java.util.List;

public class ListingDetailActivity extends ActionBarActivity {

    public static final String EXTRA_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        long id = getIntent().getLongExtra(EXTRA_ID, 0);
        if (id > 0) {
            loadDetail(id);
        }
    }

    private void loadDetail(long id) {
        new Client.Builder(ListingResult.class).id(id)
                .execAsync(new Client.RestListener<ListingResult>() {
                    @Override
                    public void onComplete(ListingResult result) {
                        List<Listing> listings = result.getResults();
                        if (listings != null && !listings.isEmpty()) {
                            setupFragments(listings.get(0));
                        }
                    }

                    @Override
                    public void onFail(Exception ex) {

                    }
                });
    }

    private void setupFragments(Listing listing) {

        DetailFragmentPagerAdapter adapter = new DetailFragmentPagerAdapter(this);

        // Adds tab Overview
        ListingOverviewFragment overviewFragment = ListingOverviewFragment.newInstance(listing);
        adapter.add(getString(R.string.tab_overview), ((Object) overviewFragment).getClass(), overviewFragment.getArguments());

        // Adds tab Description
        DescriptionFragment descriptionFragment = DescriptionFragment.newInstance(listing.getTitle(), listing.getDescription());
        adapter.add(getString(R.string.tab_description), ((Object) descriptionFragment).getClass(), descriptionFragment.getArguments());

        // Adds reviews
        ReviewListFragment reviewListFragment = ReviewListFragment.newInstance(listing.getId(), ReviewModule.LISTING);
        adapter.add(getString(R.string.tab_reviews), reviewListFragment.getClass(), reviewListFragment.getArguments());

        // Adds tab gallery
        GalleryFragment galleryFragment = GalleryFragment.newInstance(listing.getIGallery());
        adapter.add(getString(R.string.tab_gallery), ((Object) galleryFragment).getClass(), galleryFragment.getArguments());

        PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.tabStrip);
        tabStrip.setBackgroundColor(getResources().getColor(R.color.pagerStripBackground));
        tabStrip.setDrawFullUnderline(false);
        tabStrip.setTabIndicatorColor(getResources().getColor(R.color.pagerStripIndicator));

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setVisibility(View.VISIBLE);
        pager.setOffscreenPageLimit(adapter.getCount());

    }
}

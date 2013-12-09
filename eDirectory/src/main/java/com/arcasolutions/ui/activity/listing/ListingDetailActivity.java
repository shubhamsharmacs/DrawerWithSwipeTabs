package com.arcasolutions.ui.activity.listing;

import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.ReviewModule;
import com.arcasolutions.api.model.Deal;
import com.arcasolutions.api.model.DealResult;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.api.model.ListingResult;
import com.arcasolutions.ui.activity.SecondLevelActivity;
import com.arcasolutions.ui.adapter.DetailFragmentPagerAdapter;
import com.arcasolutions.ui.fragment.DescriptionFragment;
import com.arcasolutions.ui.fragment.GalleryFragment;
import com.arcasolutions.ui.fragment.ReviewListFragment;
import com.arcasolutions.ui.fragment.listing.DealOverviewFragment;
import com.arcasolutions.ui.fragment.listing.ListingOverviewFragment;

import java.util.List;

public class ListingDetailActivity extends SecondLevelActivity {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_IS_DEAL = "isDeal";
    private boolean mIsDeal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mIsDeal = getIntent().getBooleanExtra(EXTRA_IS_DEAL, false);
        long id = getIntent().getLongExtra(EXTRA_ID, 0);
        if (id > 0) {
            loadListingDetail(id);
        }


    }

    private void loadListingDetail(long id) {
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

        int selectedTabIndex = 0;
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

        if (listing.getDealId() != 0) {
            DealOverviewFragment dealOverviewFragment = DealOverviewFragment.newInstance(listing.getDealId());
            adapter.add(getString(R.string.tab_deal), dealOverviewFragment.getClass(), dealOverviewFragment.getArguments());
            selectedTabIndex = mIsDeal ? 3 : selectedTabIndex;
        }

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
        pager.setCurrentItem(selectedTabIndex);

    }
}

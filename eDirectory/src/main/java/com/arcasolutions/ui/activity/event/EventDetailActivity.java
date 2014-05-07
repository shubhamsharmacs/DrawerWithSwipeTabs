package com.arcasolutions.ui.activity.event;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.weedfinder.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.Event;
import com.arcasolutions.api.model.EventResult;
import com.arcasolutions.ui.activity.SecondLevelActivity;
import com.arcasolutions.ui.adapter.DetailFragmentPagerAdapter;
import com.arcasolutions.ui.fragment.DescriptionFragment;
import com.arcasolutions.ui.fragment.GalleryFragment;
import com.arcasolutions.ui.fragment.event.EventOverviewFragment;

import java.util.List;

public class EventDetailActivity extends SecondLevelActivity {

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
        new Client.Builder(EventResult.class).id(id)
                .execAsync(new Client.RestListener<EventResult>() {
                    @Override
                    public void onComplete(EventResult result) {
                        List<Event> events = result.getResults();
                        if (events != null && !events.isEmpty()) {
                            setupFragments(events.get(0));
                        }
                    }

                    @Override
                    public void onFail(Exception ex) {

                    }
                });
    }

    private void setupFragments(Event event) {

        DetailFragmentPagerAdapter adapter = new DetailFragmentPagerAdapter(this);

        // Adds tab Overview
        EventOverviewFragment overviewFragment = EventOverviewFragment.newInstance(event);
        adapter.add(getString(R.string.tab_overview), ((Object) overviewFragment).getClass(), overviewFragment.getArguments());

        // Adds tab Description
        DescriptionFragment descriptionFragment = DescriptionFragment.newInstance(event.getTitle(), event.getDescription());
        adapter.add(getString(R.string.tab_description), ((Object) descriptionFragment).getClass(), descriptionFragment.getArguments());

        // Adds tab gallery
        GalleryFragment galleryFragment = GalleryFragment.newInstance(event.getIGallery());
        adapter.add(getString(R.string.tab_gallery), ((Object) galleryFragment).getClass(), galleryFragment.getArguments());

        PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.tabStrip);
        tabStrip.setBackgroundColor(Color.parseColor("#3d3d3d"));
        tabStrip.setDrawFullUnderline(false);
        tabStrip.setTabIndicatorColor(Color.parseColor("#33b5e5"));

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setVisibility(View.VISIBLE);

    }
}

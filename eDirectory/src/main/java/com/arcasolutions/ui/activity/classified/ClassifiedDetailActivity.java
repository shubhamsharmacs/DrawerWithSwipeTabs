package com.arcasolutions.ui.activity.classified;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.Classified;
import com.arcasolutions.api.model.ClassifiedResult;
import com.arcasolutions.ui.activity.SecondLevelActivity;
import com.arcasolutions.ui.adapter.DetailFragmentPagerAdapter;
import com.arcasolutions.ui.fragment.DescriptionFragment;
import com.arcasolutions.ui.fragment.GalleryFragment;
import com.arcasolutions.ui.fragment.classified.ClassifiedOverviewFragment;

import java.util.List;

public class ClassifiedDetailActivity extends SecondLevelActivity {

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
        new Client.Builder(ClassifiedResult.class).id(id)
                .execAsync(new Client.RestListener<ClassifiedResult>() {
                    @Override
                    public void onComplete(ClassifiedResult result) {
                        List<Classified> classifieds = result.getResults();
                        if (classifieds != null && !classifieds.isEmpty()) {
                            setupFragments(classifieds.get(0));
                        }
                    }

                    @Override
                    public void onFail(Exception ex) {

                    }
                });
    }

    private void setupFragments(Classified classified) {

        DetailFragmentPagerAdapter adapter = new DetailFragmentPagerAdapter(this);

        // Adds tab Overview
        ClassifiedOverviewFragment overviewFragment = ClassifiedOverviewFragment.newInstance(classified);
        adapter.add(getString(R.string.tab_overview), ((Object) overviewFragment).getClass(), overviewFragment.getArguments());

        // Adds tab Description
        DescriptionFragment descriptionFragment = DescriptionFragment.newInstance(classified.getName(), classified.getDescription());
        adapter.add(getString(R.string.tab_description), ((Object) descriptionFragment).getClass(), descriptionFragment.getArguments());

        // Adds tab gallery
        GalleryFragment galleryFragment = GalleryFragment.newInstance(classified.getIGallery());
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

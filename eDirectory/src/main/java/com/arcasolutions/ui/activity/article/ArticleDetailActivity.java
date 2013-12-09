package com.arcasolutions.ui.activity.article;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.Article;
import com.arcasolutions.api.model.ArticleResult;
import com.arcasolutions.ui.activity.SecondLevelActivity;
import com.arcasolutions.ui.adapter.DetailFragmentPagerAdapter;
import com.arcasolutions.ui.fragment.GalleryFragment;
import com.arcasolutions.ui.fragment.article.ArticleOverviewFragment;

import java.util.List;

public class ArticleDetailActivity extends SecondLevelActivity {

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
        new Client.Builder(ArticleResult.class).id(id)
                .execAsync(new Client.RestListener<ArticleResult>() {
                    @Override
                    public void onComplete(ArticleResult result) {
                        List<Article> articles = result.getResults();
                        if (articles != null && !articles.isEmpty()) {
                            setupFragments(articles.get(0));
                        }
                    }

                    @Override
                    public void onFail(Exception ex) {

                    }
                });
    }

    private void setupFragments(Article article) {

        DetailFragmentPagerAdapter adapter = new DetailFragmentPagerAdapter(this);

        // Adds tab Overview
        ArticleOverviewFragment overviewFragment = ArticleOverviewFragment.newInstance(article);
        adapter.add(getString(R.string.tab_overview), overviewFragment.getClass(), overviewFragment.getArguments());


        // Adds tab gallery
        GalleryFragment galleryFragment = GalleryFragment.newInstance(article.getIGallery());
        adapter.add(getString(R.string.tab_gallery), galleryFragment.getClass(), galleryFragment.getArguments());

        PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.tabStrip);
        tabStrip.setBackgroundColor(Color.parseColor("#3d3d3d"));
        tabStrip.setDrawFullUnderline(false);
        tabStrip.setTabIndicatorColor(Color.parseColor("#33b5e5"));

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setVisibility(View.VISIBLE);

    }


}

package com.arcasolutions.ui.activity;

import android.os.Bundle;

import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.ReviewModule;
import com.arcasolutions.api.model.Review;
import com.arcasolutions.api.model.ReviewResult;
import com.arcasolutions.ui.adapter.ReviewAdapter;
import com.google.common.collect.Lists;
import com.origamilabs.library.views.StaggeredGridView;

import java.util.List;

public class HomeActivity extends BaseActivity
        implements Client.RestListener<ReviewResult> {

    private List<Review> mReviews = Lists.newArrayList();
    private StaggeredGridView mGridView;
    private ReviewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAdapter = new ReviewAdapter(this, mReviews);
        mGridView = (StaggeredGridView) findViewById(R.id.grid);
        mGridView.setAdapter(mAdapter);
        mGridView.setColumnCount(2);
        loadReviews();
    }

    private void loadReviews() {

        new Client.Builder(ReviewResult.class)
                .module(ReviewModule.LISTING)
                .id(53577)
                .execAsync(this);

    }

    @Override
    public void onComplete(ReviewResult result) {
        List<Review> reviews = result.getResults();
        if (reviews != null) {
            mReviews.addAll(reviews);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFail(Exception ex) {

    }
}

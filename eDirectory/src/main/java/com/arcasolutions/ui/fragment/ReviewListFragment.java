package com.arcasolutions.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.ReviewModule;
import com.arcasolutions.api.model.Review;
import com.arcasolutions.api.model.ReviewResult;
import com.arcasolutions.ui.adapter.ReviewAdapter;
import com.arcasolutions.util.EmptyListViewHelper;
import com.arcasolutions.util.ReviewHelper;
import com.google.common.collect.Lists;

import java.util.List;

public class ReviewListFragment extends Fragment implements Client.RestListener<ReviewResult> {

    public static final String ARG_ID = "id";
    public static final String ARG_MODULE = "module";

    private final List<Review> mReviews = Lists.newArrayList();

    private EmptyListViewHelper mEmptyHelper;
    private ReviewHelper mReviewHelper;

    private ReviewAdapter mAdapter;


    public ReviewListFragment() {}

    public static ReviewListFragment newInstance(long id, ReviewModule module) {
        final Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putSerializable(ARG_MODULE, module);

        final ReviewListFragment f = new ReviewListFragment();
        f.setArguments(args);
        return f;
    }

    public long getModuleId() {
        return getArguments() != null
                ? getArguments().getLong(ARG_ID, 0)
                : 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReviewHelper = new ReviewHelper(this, getModuleId());
        mAdapter = new ReviewAdapter(getActivity(), mReviews);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mReviewHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);

        Button addReviewButton = (Button) view.findViewById(R.id.addReviewButton);
        mReviewHelper.setAddReviewButton(addReviewButton);

        mEmptyHelper = new EmptyListViewHelper(listView, R.drawable.no_reviews, R.string.no_reviews);

        if (ReviewModule.LISTING.equals(getShownModule())) {
            new AQuery(getView()).id(R.id.addReviewButton).visible();
        }

        loadData();
    }

    private void loadData() {

        long id = getArguments() != null
                ? getArguments().getLong(ARG_ID)
                : 0;

        ReviewModule module = getShownModule();

        if (id == 0 || module == null) return;

        int mPage = 1;
        Client.Builder builder = new Client.Builder(ReviewResult.class)
                .page(mPage)
                .module(module)
                .id(id);

        builder.execAsync(this);

        mEmptyHelper.progress();
    }

    private ReviewModule getShownModule() {
        return getArguments() != null
                ? (ReviewModule) getArguments().getSerializable(ARG_MODULE)
                : null;
    }


    @Override
    public void onComplete(ReviewResult result) {
        List<Review> reviews = result.getResults();
        if (reviews != null && !reviews.isEmpty()) {
            mReviews.addAll(reviews);
        } else {
            mEmptyHelper.empty();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFail(Exception ex) {
        ex.printStackTrace();
        mEmptyHelper.error();
    }
}

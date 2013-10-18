package com.arcasolutions.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.ReviewModule;
import com.arcasolutions.api.model.Review;
import com.arcasolutions.api.model.ReviewResult;
import com.arcasolutions.ui.adapter.ReviewAdapter;
import com.google.common.collect.Lists;

import java.util.List;

public class ReviewListFragment extends Fragment implements Client.RestListener<ReviewResult> {

    public static final String ARG_ID = "id";
    public static final String ARG_MODULE = "module";

    private final List<Review> mReviews = Lists.newArrayList();

    // arguments
    private int mPage = 1;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ReviewAdapter(getActivity(), mReviews);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AQuery aq = new AQuery(view);
        aq.id(android.R.id.list).adapter(mAdapter);
        loadData();
    }

    private void loadData() {

        long id = getArguments() != null
                ? getArguments().getLong(ARG_ID)
                : 0;

        ReviewModule module = getArguments() != null
                ? (ReviewModule) getArguments().getSerializable(ARG_MODULE)
                : null;

        if (id == 0 || module == null) return;

        Client.Builder builder = new Client.Builder(ReviewResult.class)
                .page(mPage)
                .module(module)
                .id(id);

        builder.execAsync(this);
    }


    @Override
    public void onComplete(ReviewResult result) {
        List<Review> reviews = result.getResults();
        if (reviews != null) {
            mReviews.addAll(reviews);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFail(Exception ex) {
        ex.printStackTrace();
    }
}

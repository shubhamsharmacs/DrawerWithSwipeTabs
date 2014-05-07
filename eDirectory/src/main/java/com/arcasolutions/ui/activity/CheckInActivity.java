package com.arcasolutions.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ListView;

import com.weedfinder.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.CheckIn;
import com.arcasolutions.api.model.CheckInResult;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.ui.adapter.CheckInAdapter;
import com.arcasolutions.util.AbsListViewHelper;
import com.arcasolutions.util.CheckInHelper;
import com.arcasolutions.util.EmptyListViewHelper;

import java.util.LinkedList;
import java.util.List;

public class CheckInActivity extends ActionBarActivity
        implements AbsListViewHelper.OnNextPageListener,
                Client.RestListener<CheckInResult>,
                CheckInHelper.OnCheckInPostListener {

    public static final String EXTRA_LISTING = "listing";

    private final LinkedList<CheckIn> mCheckIns = new LinkedList<CheckIn>();
    private CheckInAdapter mAdapter;
    private Listing mListing;
    private AbsListViewHelper mListHelper;
    private EmptyListViewHelper mEmptyListHelper;
    private int mPage = 1;
    private CheckInHelper mCheckInHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkins);

        mListing = getIntent().getParcelableExtra(EXTRA_LISTING);
        if (mListing == null) {
            finish();
            return;
        }

        setTitle(mListing.getTitle());
        getSupportActionBar().setSubtitle(R.string.check_ins);

        mAdapter = new CheckInAdapter(this, mCheckIns);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);
        mListHelper = new AbsListViewHelper(listView, this);
        mEmptyListHelper = new EmptyListViewHelper(listView, R.drawable.no_results);

        Button checkInButton = (Button) findViewById(R.id.checkInButton);

        mCheckInHelper = new CheckInHelper(this, mListing);
        mCheckInHelper.setCheckInButton(checkInButton);

        loadCheckIns();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCheckInHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNextPage() {
        mPage += 1;
        loadCheckIns();
    }

    private void loadCheckIns() {
        mEmptyListHelper.progress();
        new Client.Builder(CheckInResult.class)
                .id(mListing.getId())
                .page(mPage)
                .execAsync(this);
    }

    @Override
    public void onComplete(CheckInResult result) {

        mListHelper.changeBaseResult(result);

        if (!TextUtils.isEmpty(result.getError())) {
            mEmptyListHelper.error();
        }

        List<CheckIn> checkIns = result.getResults();
        if (checkIns != null && !checkIns.isEmpty()) {
            mCheckIns.addAll(checkIns);
        } else {
            mEmptyListHelper.empty();
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFail(Exception ex) {
        mEmptyListHelper.error();
    }

    @Override
    public void onCheckInPosted() {
        mCheckIns.clear();
        loadCheckIns();
    }
}

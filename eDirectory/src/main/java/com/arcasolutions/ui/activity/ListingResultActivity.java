package com.arcasolutions.ui.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.api.model.ListingResult;
import com.arcasolutions.ui.adapter.ListingResultAdapter;
import com.google.android.gms.internal.bu;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class ListingResultActivity extends ActionBarActivity {

    public static final String EXTRA_ITEMS = "items";
    public static final String EXTRA_CATEGORY = "category";

    private BaseCategory mCategory;
    private int mPage;

    private final List<Listing> mListings = Lists.newArrayList();

    private ListingResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_list_view);


        mAdapter = new ListingResultAdapter(this, mListings);

        AQuery aq = new AQuery(this);
        aq.id(android.R.id.list)
                .adapter(mAdapter);

        ArrayList<Parcelable> items = getIntent().getParcelableArrayListExtra(EXTRA_ITEMS);
        if (items != null) {
            for (Parcelable p : items) {
                if (p instanceof Listing) {
                    mListings.add((Listing) p);
                }
            }
            mAdapter.notifyDataSetChanged();
        }

        mCategory = getIntent().getParcelableExtra(EXTRA_CATEGORY);
        loadListings();

    }

    private void loadListings() {

        Client.Builder builder = new Client.Builder(ListingResult.class);
        builder.page(mPage);
        if (mCategory != null) {
            builder.categoryId(mCategory.getId())
                .searchBy(SearchBy.CATEGORY);
        }
        builder.execAsync(new Client.RestListener<ListingResult>() {

            @Override
            public void onComplete(ListingResult result) {
                List<Listing> listings = result.getResults();
                if (listings != null) {
                    mListings.addAll(listings);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(Exception ex) {
                ex.printStackTrace();
            }
        });

    }


}

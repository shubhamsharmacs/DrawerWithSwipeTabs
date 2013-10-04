package com.arcasolutions.ui.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.Deal;
import com.arcasolutions.api.model.DealResult;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.ui.adapter.ModuleResultAdapter;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class DealResultActivity extends ActionBarActivity {

    public static final String EXTRA_ITEMS = "items";
    public static final String EXTRA_CATEGORY = "category";

    private BaseCategory mCategory;
    private int mPage;

    private final List<Deal> mDeals = Lists.newArrayList();

    private ModuleResultAdapter<Deal> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_list_view);


        mAdapter = new ModuleResultAdapter<Deal>(this, mDeals);

        AQuery aq = new AQuery(this);
        aq.id(android.R.id.list)
                .adapter(mAdapter);

        ArrayList<Parcelable> items = getIntent().getParcelableArrayListExtra(EXTRA_ITEMS);
        if (items != null) {
            for (Parcelable p : items) {
                if (p instanceof Listing) {
                    mDeals.add((Deal) p);
                }
            }
            mAdapter.notifyDataSetChanged();
        }

        mCategory = getIntent().getParcelableExtra(EXTRA_CATEGORY);
        loadDeals();

    }

    private void loadDeals() {

        Client.Builder builder = new Client.Builder(DealResult.class);
        builder.page(mPage);
        if (mCategory != null) {
            builder.categoryId(mCategory.getId())
                .searchBy(SearchBy.CATEGORY);
        }
        builder.execAsync(new Client.RestListener<DealResult>() {

            @Override
            public void onComplete(DealResult result) {
                List<Deal> deals = result.getResults();
                if (deals != null) {
                    mDeals.addAll(deals);
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

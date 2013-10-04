package com.arcasolutions.ui.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.Classified;
import com.arcasolutions.api.model.ClassifiedResult;
import com.arcasolutions.ui.adapter.ModuleResultAdapter;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class ClassifiedResultActivity extends ActionBarActivity {

    public static final String EXTRA_ITEMS = "items";
    public static final String EXTRA_CATEGORY = "category";

    private BaseCategory mCategory;
    private int mPage;

    private final List<Classified> mClassifieds = Lists.newArrayList();

    private ModuleResultAdapter<Classified> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_list_view);


        mAdapter = new ModuleResultAdapter<Classified>(this, mClassifieds);

        AQuery aq = new AQuery(this);
        aq.id(android.R.id.list)
                .adapter(mAdapter);

        ArrayList<Parcelable> items = getIntent().getParcelableArrayListExtra(EXTRA_ITEMS);
        if (items != null) {
            for (Parcelable p : items) {
                if (p instanceof Classified) {
                    mClassifieds.add((Classified) p);
                }
            }
            mAdapter.notifyDataSetChanged();
        }

        mCategory = getIntent().getParcelableExtra(EXTRA_CATEGORY);
        loadClassifieds();

    }

    private void loadClassifieds() {

        Client.Builder builder = new Client.Builder(ClassifiedResult.class);
        builder.page(mPage);
        if (mCategory != null) {
            builder.categoryId(mCategory.getId())
                .searchBy(SearchBy.CATEGORY);
        }
        builder.execAsync(new Client.RestListener<ClassifiedResult>() {

            @Override
            public void onComplete(ClassifiedResult result) {
                List<Classified> classifieds = result.getResults();
                if (classifieds != null) {
                    mClassifieds.addAll(classifieds);
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

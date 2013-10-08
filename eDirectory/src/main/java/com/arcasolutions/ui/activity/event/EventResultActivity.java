package com.arcasolutions.ui.activity.event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.Event;
import com.arcasolutions.api.model.EventResult;
import com.arcasolutions.ui.adapter.ModuleResultAdapter;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class EventResultActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    public static final String EXTRA_ITEMS = "items";
    public static final String EXTRA_CATEGORY = "category";

    private BaseCategory mCategory;
    private int mPage;

    private final List<Event> mEvents = Lists.newArrayList();

    private ModuleResultAdapter<Event> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_list_view);


        mAdapter = new ModuleResultAdapter<Event>(this, mEvents);

        AQuery aq = new AQuery(this);
        aq.id(android.R.id.list)
                .itemClicked(this)
                .adapter(mAdapter);

        ArrayList<Parcelable> items = getIntent().getParcelableArrayListExtra(EXTRA_ITEMS);
        if (items != null) {
            for (Parcelable p : items) {
                if (p instanceof Event) {
                    mEvents.add((Event) p);
                }
            }
            mAdapter.notifyDataSetChanged();
        }

        mCategory = getIntent().getParcelableExtra(EXTRA_CATEGORY);
        loadEvents();

    }

    private void loadEvents() {

        Client.Builder builder = new Client.Builder(EventResult.class);
        builder.page(mPage);
        if (mCategory != null) {
            builder.categoryId(mCategory.getId())
                .searchBy(SearchBy.CATEGORY);
        }
        builder.execAsync(new Client.RestListener<EventResult>() {

            @Override
            public void onComplete(EventResult result) {
                List<Event> events = result.getResults();
                if (events != null) {
                    mEvents.addAll(events);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(Exception ex) {
                ex.printStackTrace();
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.EXTRA_ID, l);
        startActivity(intent);
    }
}

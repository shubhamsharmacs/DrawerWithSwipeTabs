package com.arcasolutions.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Classified;
import com.arcasolutions.api.model.Event;

import java.util.List;
import java.util.Locale;

public class EventResultAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Event> mEvents;

    public EventResultAdapter(Context context, List<Event> events) {
        mEvents = events;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mEvents == null) return 0;
        return mEvents.size();
    }

    @Override
    public Event getItem(int i) {
        if (mEvents == null) return null;
        return mEvents.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (mEvents == null) return 0;
        return mEvents.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = mInflater.inflate(R.layout.simple_list_item_classified, viewGroup, false);
        }
        Event e = getItem(i);
        if (e != null) {
            AQuery aq = new AQuery(v);
            aq.id(R.id.eventImage).image(e.getImageUrl(), true, true);
            aq.id(R.id.eventTitle).text(e.getTitle());
            aq.id(R.id.eventDate).text(String.format(Locale.getDefault(), "%1$tD", e.getStartDate()));
            aq.id(R.id.classifiedAddress).text(e.getAddress());
        }

        return v;
    }
}

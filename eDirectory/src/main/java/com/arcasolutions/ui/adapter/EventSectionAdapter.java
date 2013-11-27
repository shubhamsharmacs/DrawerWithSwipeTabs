package com.arcasolutions.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Event;
import com.arcasolutions.view.amazing.AmazingAdapter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EventSectionAdapter extends AmazingAdapter {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
    private final LayoutInflater mInflater;
    private final List<Event> mEvents;
    private final List<Pair<String, List<Event>>> all = Lists.newArrayList();


    public EventSectionAdapter(Context context, List<Event> events) {
        mInflater = LayoutInflater.from(context);
        mEvents = events;
        organizeData();
    }

    @Override
    public void notifyDataSetChanged() {
        organizeData();
        super.notifyDataSetChanged();
    }

    private void organizeData() {

        synchronized (mEvents) {

            Collections.sort(mEvents);

            all.clear();

            if (mEvents != null) {
                Map<String, List<Event>> eventMap = Maps.newLinkedHashMap();
                for (Event e : mEvents) {
                    String date = FORMAT.format(e.getStartDate());
                    List<Event> events = eventMap.get(date);
                    if (events == null) events = Lists.newArrayList();
                    events.add(e);
                    eventMap.put(date, events);
                }

                for (Map.Entry<String, List<Event>> entry : eventMap.entrySet()) {
                    all.add(Pair.create(entry.getKey(), entry.getValue()));
                }
            }

        }
    }



    @Override
    protected void onNextPageRequested(int page) {
    }

    @Override
    protected void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
        View headerView = view.findViewById(R.id.header);
        if (displaySectionHeader) {
            headerView.setVisibility(View.VISIBLE);
            TextView sectionTitle = (TextView) headerView;
            sectionTitle.setText(getSections()[getSectionForPosition(position)]);
        } else {
            headerView.setVisibility(View.GONE);
        }
    }

    @Override
    public View getAmazingView(int position, View convertView, ViewGroup parent) {
        Event e = getItem(position);
        if (convertView == null) convertView = mInflater.inflate(R.layout.simple_list_item_section_event, parent, false);
        View res = ModuleResultAdapter.getView(mInflater, e, convertView, parent);
        AQuery aq = new AQuery(res);
        aq.id(R.id.eventImage).gone();
        return res;
    }

    @Override
    public void configurePinnedHeader(View header, int position, int alpha) {
        TextView headerView = (TextView) header;
        headerView.setText(getSections()[getSectionForPosition(position)]);
        headerView.setBackgroundColor(alpha << 24 | (0xbdc3c7));
        headerView.setTextColor(alpha << 24 | (0xffffff));
    }

    @Override
    public int getPositionForSection(int section) {
        if (section < 0) section = 0;
        if (section >= all.size()) section = all.size() - 1;
        int c = 0;
        for (int i = 0; i < all.size(); i++) {
            if (section == i) {
                return c;
            }
            c += all.get(i).second.size();
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        int c = 0;
        for (int i = 0; i < all.size(); i++) {
            if (position >= c && position < c + all.get(i).second.size()) {
                return i;
            }
            c += all.get(i).second.size();
        }
        return -1;
    }

    @Override
    public String[] getSections() {
        String[] res = new String[all.size()];
        for (int i = 0; i < all.size(); i++) {
            res[i] = all.get(i).first;
        }
        return res;
    }

    @Override
    public int getCount() {
        int res = 0;
        for (int i = 0; i < all.size(); i++) {
            res += all.get(i).second.size();
        }
        return res;
    }

    @Override
    public Event getItem(int position) {
        int c = 0;
        for (int i = 0; i < all.size(); i++) {
            if (position >= c && position < c + all.get(i).second.size()) {
                return all.get(i).second.get(position - c);
            }
            c += all.get(i).second.size();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        Event e = getItem(position);
        return e != null
                ? e.getId()
                : 0;
    }
}

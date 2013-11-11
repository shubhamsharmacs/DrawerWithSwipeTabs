package com.arcasolutions.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.arcasolutions.R;

public class EmptyListViewHelper {

    private Context mContext;
    private ListView mListView;
    private int mEmptyDrawableId;

    public EmptyListViewHelper(ListView listView, int emptyDrawableId) {
        mListView = listView;
        mEmptyDrawableId = emptyDrawableId;
        mContext = mListView.getContext();
    }

    public void empty() {
        ViewGroup parent = (ViewGroup) mListView.getParent();
        if (parent != null) {
            View existingEmptyView = parent.findViewById(android.R.id.empty);
            if (existingEmptyView != null) {
                parent.removeView(existingEmptyView);
            }
            View emptyView = getEmptyView();
            parent.addView(emptyView);
            mListView.setEmptyView(emptyView);
        }
    }

    private View getEmptyView() {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View emptyView = mInflater.inflate(R.layout.custom_empty_view, null);
        emptyView.setId(android.R.id.empty);
        AQuery aq = new AQuery(emptyView);
        aq.id(R.id.image).image(mEmptyDrawableId);
        return emptyView;
    }

}

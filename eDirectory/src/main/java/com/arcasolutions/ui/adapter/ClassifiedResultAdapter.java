package com.arcasolutions.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Article;
import com.arcasolutions.api.model.Classified;

import java.util.List;

public class ClassifiedResultAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Classified> mClassifieds;

    public ClassifiedResultAdapter(Context context, List<Classified> classifieds) {
        mClassifieds = classifieds;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mClassifieds == null) return 0;
        return mClassifieds.size();
    }

    @Override
    public Classified getItem(int i) {
        if (mClassifieds == null) return null;
        return mClassifieds.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (mClassifieds == null) return 0;
        return mClassifieds.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = mInflater.inflate(R.layout.simple_list_item_classified, viewGroup, false);
        }
        Classified c = getItem(i);
        if (c != null) {
            AQuery aq = new AQuery(v);
            aq.id(R.id.classifiedImage).image(c.getImageUrl(), true, true);
            aq.id(R.id.classifiedTitle).text(c.getName());
            aq.id(R.id.classifiedPrice).text(String.format("$%.2f", c.getPrice()));
            aq.id(R.id.classifiedAddress).text(c.getAddress());
        }

        return v;
    }
}

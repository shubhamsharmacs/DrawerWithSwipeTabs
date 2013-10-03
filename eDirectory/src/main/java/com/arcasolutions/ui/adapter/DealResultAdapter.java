package com.arcasolutions.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Deal;

import java.util.List;

public class DealResultAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Deal> mDeals;

    public DealResultAdapter(Context context, List<Deal> deals) {
        mDeals = deals;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mDeals == null) return 0;
        return mDeals.size();
    }

    @Override
    public Deal getItem(int i) {
        if (mDeals == null) return null;
        return mDeals.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (mDeals == null) return 0;
        return mDeals.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = mInflater.inflate(R.layout.simple_list_item_deal, viewGroup, false);
        }
        Deal d = getItem(i);
        if (d != null) {
            AQuery aq = new AQuery(v);
            aq.id(R.id.dealImage).image(d.getImageUrl(), true, true);
            aq.id(R.id.dealName).text(d.getTitle());
            aq.id(R.id.dealPrice).text(String.format("$%.2f", d.getDealValue()));
            aq.id(R.id.dealDiscount).text(String.format("%d%%", (int)(Math.ceil((1 - (d.getDealValue() / d.getRealValue()) ) * 100) )));
            aq.id(R.id.dealListing).text(d.getListingTitle());
        }

        return v;
    }
}

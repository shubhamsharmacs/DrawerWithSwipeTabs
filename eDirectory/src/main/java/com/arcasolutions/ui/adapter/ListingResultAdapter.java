package com.arcasolutions.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Listing;

import java.util.List;

public class ListingResultAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Listing> mListings;

    public ListingResultAdapter(Context context, List<Listing> listings) {
        mListings = listings;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mListings == null) return 0;
        return mListings.size();
    }

    @Override
    public Listing getItem(int i) {
        if (mListings == null) return null;
        return mListings.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (mListings == null) return 0;
        return mListings.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = mInflater.inflate(R.layout.simple_list_item_listing, viewGroup, false);
        }
        Listing l = getItem(i);
        if (l != null) {
            AQuery aq = new AQuery(v);
            aq.id(R.id.listingImage).image(l.getImageUrl(), true, true);
            aq.id(R.id.listingTitle).text(l.getTitle());
            aq.id(R.id.listingAddress).text(l.getAddress());
            aq.id(R.id.listingReviews).text(String.format("%d reviews", l.getTotalReviews()));
            aq.id(R.id.listingRatingBar).rating(l.getRating());
            aq.id(R.id.listingDealBadge).getView()
                    .setVisibility(l.getDealId() != 0 ? View.VISIBLE : View.GONE);
        }

        return v;
    }
}

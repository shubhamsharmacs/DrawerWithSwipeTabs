package com.arcasolutions.ui.display;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Listing;

public final class ListingDisplay {

    public static View onViewResult(Listing l, LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.simple_list_item_listing, viewGroup, false);
        }

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

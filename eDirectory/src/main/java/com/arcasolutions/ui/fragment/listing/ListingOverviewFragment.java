package com.arcasolutions.ui.fragment.listing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Classified;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.ui.fragment.ContactInfoFragment;

import java.util.Locale;

public class ListingOverviewFragment extends Fragment {

    public static final String ARG_LISTING = "listing";

    public ListingOverviewFragment() {}

    public static ListingOverviewFragment newInstance(Listing listing) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_LISTING, listing);
        final ListingOverviewFragment f = new ListingOverviewFragment();
        f.setArguments(args);
        return f;
    }

    public Listing getShownListing() {
        return getArguments() != null
                ? (Listing) getArguments().getParcelable(ARG_LISTING)
                : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listing_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Listing l = getShownListing();
        if (l != null) {
            AQuery aq = new AQuery(view);
            aq.id(R.id.listingOverviewImage).image(l.getImageUrl(), true, true);
            aq.id(R.id.listingOverviewTitle).text(l.getTitle());
            aq.id(R.id.listingOverviewSummary).text(l.getSummary());
            aq.id(R.id.listingOverviewAddress).text(l.getAddress()).getView().invalidate();
            aq.id(R.id.listingOverviewRatingBar).rating(l.getRating());
            aq.id(R.id.listingOverviewReviews).text(String.format("%d reviews", l.getTotalReviews()));

            ContactInfoFragment f = ContactInfoFragment.newInstance(l);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.listingOverviewContact, f)
                    .commit();
        }

    }
}

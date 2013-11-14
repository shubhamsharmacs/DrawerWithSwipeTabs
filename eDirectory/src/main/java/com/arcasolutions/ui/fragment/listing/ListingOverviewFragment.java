package com.arcasolutions.ui.fragment.listing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.ui.fragment.ContactInfoFragment;
import com.arcasolutions.util.CheckInHelper;
import com.arcasolutions.util.FavoriteHelper;

public class ListingOverviewFragment extends Fragment {

    public static final String ARG_LISTING = "listing";
    private CheckInHelper mCheckInHelper;

    private FavoriteHelper<Listing> mFavoriteHelper;

    public ListingOverviewFragment() {
    }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoriteHelper = new FavoriteHelper<Listing>(getActivity(), Listing.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listing_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Listing l = getShownListing();
        if (l != null) {
            mFavoriteHelper.updateFavorite(l);

            AQuery aq = new AQuery(view);
            aq.id(R.id.listingOverviewImage).image(l.getImageUrl(), true, true);
            if (!URLUtil.isValidUrl(l.getImageUrl())) {
                aq.id(R.id.listingOverviewImage).gone();
                aq.id(R.id.listingOverviewFavorite).margin(0, 0, getResources().getDimension(R.dimen.spacingSmall), 0);
            }

            aq.id(R.id.listingOverviewTitle).text(l.getTitle());
            aq.id(R.id.listingOverviewSummary).text(l.getSummary());
            aq.id(R.id.listingOverviewAddress).text(l.getAddress()).getView().invalidate();
            aq.id(R.id.listingOverviewRatingBar).rating(l.getRating());
            aq.id(R.id.listingOverviewReviews).text(String.format("%d reviews", l.getTotalReviews()));
            final CheckBox favoriteCheckBox = aq.id(R.id.listingOverviewFavorite).getCheckBox();
            favoriteCheckBox.setChecked(mFavoriteHelper.isFavorited(l));
            favoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (!mFavoriteHelper.toggleFavorite(l)) {
                        favoriteCheckBox.setChecked(!b);
                    }
                }
            });

            ContactInfoFragment f = ContactInfoFragment.newInstance(l);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.listingOverviewContact, f)
                    .commit();

            mCheckInHelper = new CheckInHelper(this, l);
            mCheckInHelper.setCheckInButton(aq.id(R.id.listingOverviewCheckIn).getButton());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCheckInHelper.onActivityResult(requestCode, resultCode, data);
    }
}

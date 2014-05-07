package com.arcasolutions.ui.fragment.listing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.androidquery.AQuery;
import com.weedfinder.R;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.ui.fragment.BaseFragment;
import com.arcasolutions.ui.fragment.ContactInfoFragment;
import com.arcasolutions.util.FavoriteHelper;
import com.arcasolutions.util.IntentUtil;

public class ListingOverviewFragment extends BaseFragment {

    public static final String ARG_LISTING = "listing";

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
            doShare(IntentUtil.share(l.getTitle(), l.getFriendlyUrl()));

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
            aq.id(R.id.listingOverviewReviews).text(getResources().getQuantityString(R.plurals.X_reviews, l.getTotalReviews(), l.getTotalReviews()));
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

            aq.id(R.id.listingOverviewCall).clicked(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtil.call(getActivity(), l.getPhoneNumber());
                }
            });

            showMapIfNeeded(l);
        }

    }

}

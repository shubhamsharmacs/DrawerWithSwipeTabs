package com.arcasolutions.ui.fragment.listing;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.Deal;
import com.arcasolutions.api.model.DealResult;
import com.arcasolutions.util.AccountHelper;
import com.arcasolutions.util.FavoriteHelper;
import com.arcasolutions.util.FmtUtil;
import com.arcasolutions.util.RedeemHelper;

import java.util.List;
import java.util.Locale;

public class DealOverviewFragment extends Fragment implements Client.RestListener<BaseResult> {

    public static final String ARG_DEAL_ID = "dealId";

    private FavoriteHelper<Deal> mFavoriteHelper;
    private RedeemHelper mRedeemHelper;

    public DealOverviewFragment() {
    }

    public static DealOverviewFragment newInstance(long id) {
        final Bundle args = new Bundle();
        args.putLong(ARG_DEAL_ID, id);
        final DealOverviewFragment f = new DealOverviewFragment();
        f.setArguments(args);
        return f;
    }

    public long getShownDealId() {
        return getArguments() != null
                ? getArguments().getLong(ARG_DEAL_ID, 0)
                : 0L;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoriteHelper = new FavoriteHelper<Deal>(getActivity(), Deal.class);
        mRedeemHelper = RedeemHelper.from(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deal_overview, container, false);
    }

    public void showTerms(View v) {
        Object tag = v.getTag();
        if (tag != null && tag instanceof String) {
            String terms = (String) tag;
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.terms_and_conditions)
                    .setMessage(terms)
                    .setPositiveButton(R.string.ok, null)
                    .create().show();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Client.Builder builder = new Client.Builder(DealResult.class);
        builder.id(getShownDealId());

        AccountHelper accountHelper = new AccountHelper(getActivity());
        if (accountHelper.hasAccount()) {
            builder.accountId(accountHelper.getAccount().getId());
        }

        builder.execAsync(this);
    }

    private void populate(final Deal d) {
        if (d != null) {
            mFavoriteHelper.updateFavorite(d);

            AQuery aq = new AQuery(getView());
            aq.id(R.id.dealOverviewImage).image(d.getImageUrl(), true, true);
            if (!URLUtil.isValidUrl(d.getImageUrl())) {
                aq.id(R.id.dealOverviewImage).gone();
                aq.id(R.id.dealOverviewFavorite).margin(0, 0, getResources().getDimension(R.dimen.spacingSmall), 0);
            }

            mRedeemHelper.init(R.id.redeemPlace, d);

            aq.id(R.id.dealOverviewTitle).text(d.getTitle());
            aq.id(R.id.dealOverviewDescription).text(d.getDescription());
            aq.id(R.id.dealOverviewRatingBar).rating(d.getRating());
            aq.id(R.id.dealOverviewReviews).text(String.format("%d reviews", d.getTotalReviews()));
            TextView originalPriceView = aq.id(R.id.dealOverviewOriginalPrice).text(String.format("$%.2f", d.getRealValue())).getTextView();
            originalPriceView.setPaintFlags(originalPriceView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); // tacha o texto
            aq.id(R.id.dealOverviewDealPrice).text(String.format("$%.2f", d.getDealValue()));
            aq.id(R.id.dealOverviewStartDate).text(String.format(Locale.getDefault(), "%1$ta %1$td, %1$tY", d.getStartDate()));
            aq.id(R.id.dealOverviewEndDate).text(String.format(Locale.getDefault(), "%1$ta %1$td, %1$tY", d.getEndDate()));
            aq.id(R.id.dealOverviewRemain).text(Integer.toString(d.getAmount()));
            aq.id(R.id.dealOverviewDiscount).text(String.format(Locale.getDefault(), "%1$d%% OFF", (int) ((1 - (d.getDealValue() / d.getRealValue())) * 100)) );
            aq.id(R.id.dealOverviewTerms).tag(d.getConditions()).clicked(this, "showTerms");

            final CheckBox favoriteCheckBox = aq.id(R.id.dealOverviewFavorite).getCheckBox();
            favoriteCheckBox.setChecked(mFavoriteHelper.isFavorited(d));
            favoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (!mFavoriteHelper.toggleFavorite(d)) {
                        favoriteCheckBox.setChecked(!b);
                    }
                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRedeemHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onComplete(BaseResult result) {
        List<Deal> deals = result.getResults();
        if (deals != null && !deals.isEmpty()) {
            populate(deals.get(0));
        }
    }

    @Override
    public void onFail(Exception ex) {

    }
}

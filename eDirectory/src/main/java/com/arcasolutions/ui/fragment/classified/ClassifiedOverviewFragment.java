package com.arcasolutions.ui.fragment.classified;

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
import com.arcasolutions.api.model.Classified;
import com.arcasolutions.ui.fragment.ContactInfoFragment;
import com.arcasolutions.util.FavoriteHelper;

import java.util.Locale;

public class ClassifiedOverviewFragment extends Fragment {

    public static final String ARG_CLASSIFIED = "classified";
    private FavoriteHelper<Classified> mFavoriteHelper;

    public ClassifiedOverviewFragment() {
    }

    public static ClassifiedOverviewFragment newInstance(Classified classified) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_CLASSIFIED, classified);
        final ClassifiedOverviewFragment f = new ClassifiedOverviewFragment();
        f.setArguments(args);
        return f;
    }

    public Classified getShownClassified() {
        return getArguments() != null
                ? (Classified) getArguments().getParcelable(ARG_CLASSIFIED)
                : null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoriteHelper = new FavoriteHelper<Classified>(getActivity(), Classified.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classified_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Classified c = getShownClassified();
        if (c != null) {
            mFavoriteHelper.updateFavorite(c);

            AQuery aq = new AQuery(view);
            aq.id(R.id.classifiedOverviewImage).image(c.getImageUrl(), true, true);
            if (!URLUtil.isValidUrl(c.getImageUrl())) {
                aq.id(R.id.classifiedOverviewImage).gone();
                aq.id(R.id.classifiedOverviewFavorite).margin(0, 0, getResources().getDimension(R.dimen.spacingSmall), 0);
            }
            aq.id(R.id.classifiedOverviewTitle).text(c.getName());
            aq.id(R.id.classifiedOverviewDescription).text(c.getSummary());
            aq.id(R.id.classifiedOverviewAddress).text(c.getAddress()).getView().invalidate();
            aq.id(R.id.classifiedOverviewPrice).text(String.format(Locale.getDefault(), "%.2f", c.getPrice()));
            final CheckBox favoriteCheckBox = aq.id(R.id.classifiedOverviewFavorite).getCheckBox();
            favoriteCheckBox.setChecked(mFavoriteHelper.isFavorited(c));
            favoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (!mFavoriteHelper.toggleFavorite(c)) {
                        favoriteCheckBox.setChecked(!b);
                    }
                }
            });

            ContactInfoFragment f = ContactInfoFragment.newInstance(c);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.classifiedOverviewContact, f)
                    .commit();
        }

    }
}

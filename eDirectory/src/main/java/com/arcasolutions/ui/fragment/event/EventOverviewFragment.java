package com.arcasolutions.ui.fragment.event;

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
import com.arcasolutions.api.model.Event;
import com.arcasolutions.ui.fragment.ContactInfoFragment;
import com.arcasolutions.util.FavoriteHelper;

import java.util.Locale;

public class EventOverviewFragment extends Fragment {

    public static final String ARG_EVENT = "event";
    private FavoriteHelper<Event> mFavoriteHelper;

    public EventOverviewFragment() {
    }

    public static EventOverviewFragment newInstance(Event event) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT, event);
        final EventOverviewFragment f = new EventOverviewFragment();
        f.setArguments(args);
        return f;
    }

    public Event getShownEvent() {
        return getArguments() != null
                ? (Event) getArguments().getParcelable(ARG_EVENT)
                : null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoriteHelper = new FavoriteHelper<Event>(getActivity(), Event.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Event e = getShownEvent();
        if (e != null) {
            mFavoriteHelper.updateFavorite(e);

            AQuery aq = new AQuery(view);
            aq.id(R.id.eventOverviewImage).image(e.getImageUrl(), true, true);
            if (!URLUtil.isValidUrl(e.getImageUrl())) {
                aq.id(R.id.eventOverviewImage).gone();
                aq.id(R.id.eventOverviewFavorite).margin(0, 0, getResources().getDimension(R.dimen.spacingSmall),0);
            }
            aq.id(R.id.eventOverviewTitle).text(e.getTitle());
            aq.id(R.id.eventOverviewDescription).text(e.getSummary());
            aq.id(R.id.eventOverviewAddress).text(e.getAddress()).getView().invalidate();
            aq.id(R.id.eventOverviewStartDate).text(String.format(Locale.getDefault(), "%tD", e.getStartDate()));
            aq.id(R.id.eventOverviewEndDate).text(String.format(Locale.getDefault(), "%tD", e.getEndDate()));

            final CheckBox favoriteCheckBox = aq.id(R.id.eventOverviewFavorite).getCheckBox();
            favoriteCheckBox.setChecked(mFavoriteHelper.isFavorited(e));
            favoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (!mFavoriteHelper.toggleFavorite(e)) {
                        favoriteCheckBox.setChecked(!b);
                    }
                }
            });

            ContactInfoFragment f = ContactInfoFragment.newInstance(e);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.eventOverviewContact, f)
                    .commit();
        }

    }
}

package com.arcasolutions.ui.fragment.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Event;
import com.arcasolutions.ui.fragment.ContactInfoFragment;

import java.util.Locale;

public class EventOverviewFragment  extends Fragment {

    public static final String ARG_EVENT = "event";

    public EventOverviewFragment() {}

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Event e = getShownEvent();
        if (e != null) {
            AQuery aq = new AQuery(view);
            aq.id(R.id.eventOverviewImage).image(e.getImageUrl(), true, true);
            aq.id(R.id.eventOverviewTitle).text(e.getTitle());
            aq.id(R.id.eventOverviewDescription).text(e.getSummary());
            aq.id(R.id.eventOverviewAddress).text(e.getAddress()).getView().invalidate();
            aq.id(R.id.eventOverviewStartDate).text(String.format(Locale.getDefault(), "%tD", e.getStartDate()));
            aq.id(R.id.eventOverviewEndDate).text(String.format(Locale.getDefault(), "%tD", e.getEndDate()));

            ContactInfoFragment f = ContactInfoFragment.newInstance(e);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.eventOverviewContact, f)
                    .commit();
        }

    }
}

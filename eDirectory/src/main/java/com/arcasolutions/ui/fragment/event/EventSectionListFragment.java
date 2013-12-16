package com.arcasolutions.ui.fragment.event;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.Event;
import com.arcasolutions.api.model.EventResult;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.OnModuleSelectionListener;
import com.arcasolutions.ui.adapter.EventSectionAdapter;
import com.arcasolutions.ui.fragment.BaseFragment;
import com.arcasolutions.ui.fragment.ModuleResultFragment;
import com.arcasolutions.util.EmptyListViewHelper;
import com.arcasolutions.view.amazing.AmazingAdapter;
import com.arcasolutions.view.amazing.AmazingListView;
import com.google.common.collect.Lists;

import java.util.Calendar;
import java.util.List;

public class EventSectionListFragment extends BaseFragment
        implements Client.RestListener<EventResult>, AdapterView.OnItemClickListener {

    private static final String TAG = "EventSectionListFragment";

    public static final String ARG_INITIAL_DATE = "initial_date";
    private static final int NUMBER_MONTHS = 12;

    private int MONTHS_LOADED_COUNT = 1;

    private AmazingListView mListView;
    private AmazingAdapter mAdapter;
    private final List<Event> mEvents = Lists.newArrayList();
    private boolean mIsThisFragmentVisible = false;
    private EmptyListViewHelper mEmptyListViewHelper;
    private boolean mIsViewCreated = false;
    private OnModuleSelectionListener mListener;

    public EventSectionListFragment() {}

    public static EventSectionListFragment newInstance(Calendar initial) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_INITIAL_DATE, initial);
        final EventSectionListFragment f = new EventSectionListFragment();
        f.setArguments(args);
        return f;
    }

    public static EventSectionListFragment newInstance() {
        return newInstance(Calendar.getInstance());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnModuleSelectionListener) {
            mListener = (OnModuleSelectionListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new EventSectionAdapter(getActivity(), mEvents);
    }

    public Calendar getShownCalendar() {
        return  getArguments() != null
                    ? (Calendar) getArguments().getSerializable(ARG_INITIAL_DATE)
                    : Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        mListView = (AmazingListView) rootView.findViewById(android.R.id.list);
        mListView.setPinnedHeaderView(inflater.inflate(R.layout.simple_list_section_event, mListView, false));
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mEmptyListViewHelper = new EmptyListViewHelper(mListView, R.drawable.no_events);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsViewCreated = true;
        loadEvents(getShownCalendar());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mListener != null) {
            Event e = mEvents.get(i);
            mListener.onModuleSelected(e, i, l);
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        mIsThisFragmentVisible = menuVisible;
        if (mIsThisFragmentVisible) {
            mEvents.clear();
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            MONTHS_LOADED_COUNT = 0;
            if (mIsViewCreated) {
                loadEvents(getShownCalendar());
            }
        }
    }

    private void loadMoreEventsIfNeeded() {
        Calendar max = (Calendar) getShownCalendar().clone();
        max.add(Calendar.MONTH, NUMBER_MONTHS);

        Calendar aux = (Calendar) getShownCalendar().clone();
        aux.add(Calendar.MONTH, NUMBER_MONTHS - (NUMBER_MONTHS - MONTHS_LOADED_COUNT));

        if (aux.compareTo(max) > 0) return;

        loadEvents(aux);
    }

    private void loadEvents(Calendar mCal) {
        if (!mIsThisFragmentVisible || mCal == null) return;

        MONTHS_LOADED_COUNT++;

        int day = mCal.get(Calendar.DATE);
        int month = mCal.get(Calendar.MONTH) + 1;
        int year = mCal.get(Calendar.YEAR);

        new Client.Builder(EventResult.class)
                .searchBy(SearchBy.CALENDAR_LIST)
                .day(day)
                .month(month)
                .year(year)
                .execAsync(this);

        mEmptyListViewHelper.progress();
        startProgress();
    }

    @Override
    public void onComplete(EventResult result) {
        finishProgress();
        List<Event> events = result.getResults();
        if (events != null) {
            mEvents.addAll(events);
        }
        mAdapter.notifyDataSetChanged();
        loadMoreEventsIfNeeded();
        mEmptyListViewHelper.empty();
    }

    @Override
    public void onFail(Exception ex) {
        finishProgress();
        Log.e(TAG, ex.getMessage(), ex);
        mEmptyListViewHelper.error();
    }

}

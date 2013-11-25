package com.arcasolutions.ui.fragment.event;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.EventSchedule;
import com.arcasolutions.api.model.EventScheduleResult;
import com.arcasolutions.ui.adapter.EventCalendarAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EventCalendarFragment extends Fragment
        implements EventCalendarAdapter.OnYearChangedListener,
            Client.RestListener<EventScheduleResult> {

    public interface OnDateChangeListener {
        public void onMonthChanged(int month, int year);
        public void onDateClicked(int day, int month, int year);
    }

    public static final String ARG_YEAR = "year";
    public static final String ARG_MONTH = "month";

    private final SimpleDateFormat FORMAT = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    private Calendar mMonth = Calendar.getInstance();
    private EventCalendarAdapter mAdapter;
    private OnDateChangeListener mDateChangeListener;

    public EventCalendarFragment() {}

    public static EventCalendarFragment newInstance() {
        return new EventCalendarFragment();
    }

    public static EventCalendarFragment newInstance(int year, int month) {
        final Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);

        final EventCalendarFragment f = new EventCalendarFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mDateChangeListener = (OnDateChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getClass()
                    + " must implement " + OnDateChangeListener.class.getName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new EventCalendarAdapter(getActivity(), mMonth, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        int year = mMonth.get(Calendar.YEAR);
        int month = mMonth.get(Calendar.MONTH);

        Bundle args = getArguments();
        if (args != null) {
            year = args.getInt(ARG_YEAR, year);
            month = args.getInt(ARG_MONTH, month);
        }

        mMonth.set(Calendar.YEAR, year);
        mMonth.set(Calendar.MONTH, month);

        loadEvents(year);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_event, container, false);

        AQuery aq = new AQuery(view);
        aq.id(R.id.gridView).itemClicked(this, "onDateClicked").adapter(mAdapter);
        aq.id(R.id.buttonPrev).clicked(this, "onMonthChange");
        aq.id(R.id.buttonNext).clicked(this, "onMonthChange");

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateViews();
    }

    public void onDateClicked(AdapterView<?> parent, View v, int pos, long id) {
        Calendar c = (Calendar) mAdapter.getItem(pos);
        if (c != null) {
            mDateChangeListener.onDateClicked(
                    c.get(Calendar.DAY_OF_MONTH),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.YEAR)
            );
        }
    }

    @Override
    public void onYearChanded(int year) {
        loadEvents(year);
    }

    public void onMonthChange(View view) {
        switch (view.getId()) {
            case R.id.buttonPrev:
                changeMonth(-1);
                break;
            case R.id.buttonNext:
                changeMonth(1);
                break;
        }
    }

    private void changeMonth(int count) {
        mMonth.add(Calendar.MONTH, count);
        mAdapter.updateMonth(mMonth);
        updateViews();
        mDateChangeListener.onMonthChanged(mMonth.get(Calendar.MONTH),
                mMonth.get(Calendar.YEAR));
    }

    private void updateViews() {
        AQuery aq = new AQuery(getView());
        aq.id(R.id.month).text(FORMAT.format(mMonth.getTime()));
    }

    private void loadEvents(int year) {
        new Client.Builder(EventScheduleResult.class)
                .searchBy(SearchBy.CALENDAR)
                .year(year)
                .execAsync(this);
        mAdapter.setEventCalendar(new EventSchedule());
    }

    @Override
    public void onComplete(EventScheduleResult result) {
        EventSchedule schedule = result.getCalendar();
        mAdapter.setEventCalendar(schedule);
    }

    @Override
    public void onFail(Exception ex) {
        ex.printStackTrace();
    }


}

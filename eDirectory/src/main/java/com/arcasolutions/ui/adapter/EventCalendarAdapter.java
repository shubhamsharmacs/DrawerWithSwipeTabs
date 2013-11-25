package com.arcasolutions.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arcasolutions.R;
import com.arcasolutions.api.model.EventSchedule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class EventCalendarAdapter extends BaseAdapter {

    // ///////////////////////////////////
    // DO NOT CHANGE THESE VALUES //
    // ///////////////////////////////////
    private static final int SUNDAY = 0;
    private static final int MONDAY = 1;
    // ///////////////////////////////////

    /**
     *
     */
    private static final int FIRST_DAY_OF_WEEK = SUNDAY; // SUNDAY = 0 MONDAY =
    // 1

    /**
     * Week labels
     * */
    private static final String[][] WEEK = new String[2][7];

    private static final int VIEW_LABEL = 0x2;
    private static final int VIEW_VALUE = 0x3;

    private Calendar mCal;
    private Calendar mSelectedMonth = null;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Calendar> mDates = Lists.newArrayList();

    /**
     * It stores month as key and values are days with events.
     */
    private Map<Integer, int[]> mEventsMap = Maps.newHashMap();

    /** Current year */
    private int mYear;
    /**
     * Number of day necessary to create the calendar including days of previous
     * and/or next month
     */
    private int size;
    /** First day of week on current month */
    private int fdw;
    /** Last day of week on current month */
    private int ldw;
    /** Last day on current month */
    private int lastDate;

    private OnYearChangedListener mListener;

    public interface OnYearChangedListener {
        public void onYearChanded(int year);
    }

    // calendar colors
    private final int mBackgroundWithEvent;
    private final int mBackgroundWithoutEvent;
    private final int mTextcolorInMonth;
    private final int mTextcolorOutMonth;

    public EventCalendarAdapter(Context context, Calendar monthCalendar,
                           OnYearChangedListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        Resources res = mContext.getResources();

        mBackgroundWithoutEvent = res.getColor(R.color.calendar_bg_normal);
        mBackgroundWithEvent = res.getColor(R.color.calendar_bg_events);
        mTextcolorOutMonth = res.getColor(R.color.calendar_text_outmonth);
        mTextcolorInMonth = res.getColor(R.color.calendar_text_inmonth);

        WEEK[SUNDAY] = res.getStringArray(R.array.DAYS_WEEK_HEBREW);
        WEEK[MONDAY] = res.getStringArray(R.array.DAYS_WEEK_GREGORIAN);

        updateMonth(monthCalendar);
        mListener = listener;
    }

    /**
     * Change display date of adapter
     *
     * @param monthCalendar
     *            Month should be shown
     */
    @SuppressWarnings("unused")
    public void updateMonth(Calendar monthCalendar) {

        int year = monthCalendar.get(Calendar.YEAR);
        if (mSelectedMonth != null && year != mYear) {
            mYear = year;
            mListener.onYearChanded(mYear);
        }

        mSelectedMonth = monthCalendar;
        mSelectedMonth.set(Calendar.DAY_OF_MONTH, 1);

        fdw = mSelectedMonth.get(Calendar.DAY_OF_WEEK);
        lastDate = mSelectedMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        mSelectedMonth.set(Calendar.DAY_OF_MONTH, lastDate);
        ldw = mSelectedMonth.get(Calendar.DAY_OF_WEEK);

        int nd2cfw = FIRST_DAY_OF_WEEK != MONDAY // number of days necessary to
                // complete first week
                ? (fdw - 1)
                : ((fdw - 1) == 0 ? 6 : fdw - 2);
        int nd2clw = ((7 + FIRST_DAY_OF_WEEK) - ldw); // number of days
        // necessary to complete
        // last week
        nd2clw = nd2clw > 6 ? 0 : nd2clw; // adjust number of days

        size = 7; // number of days on week,it's used to print week labels
        size += lastDate; // number of days in the month
        size += nd2cfw;
        size += nd2clw;

        mCal = (Calendar) monthCalendar.clone();

        mDates.clear();
        mCal.setLenient(true);
        mCal.set(Calendar.DATE, 1);
        mCal.add(Calendar.DATE, nd2cfw * -1);
        for (int i = 0; i < size - 7; i++) {
            mDates.add((Calendar) mCal.clone());
            mCal.add(Calendar.DATE, 1);
        }

        notifyDataSetChanged();
    }

    public void setEventCalendar(EventSchedule eventCalendar) {
        mEventsMap.clear();
        if (eventCalendar != null) {
            mEventsMap.put(1, eventCalendar.getJan());
            mEventsMap.put(2, eventCalendar.getFeb());
            mEventsMap.put(3, eventCalendar.getMar());
            mEventsMap.put(4, eventCalendar.getApr());
            mEventsMap.put(5, eventCalendar.getMay());
            mEventsMap.put(6, eventCalendar.getJun());
            mEventsMap.put(7, eventCalendar.getJul());
            mEventsMap.put(8, eventCalendar.getAug());
            mEventsMap.put(9, eventCalendar.getSep());
            mEventsMap.put(10, eventCalendar.getOct());
            mEventsMap.put(11, eventCalendar.getNov());
            mEventsMap.put(12, eventCalendar.getDec());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public Object getItem(int position) {
        if (position < 7 || mDates == null || mDates.isEmpty())
            return null;

        return mDates.get(position - 7);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) convertView;

        int tag = (v != null) ? (Integer) v.getTag() : -1;

        if (position < 7) { // first row of grid: weekdays

            if (v == null || tag != VIEW_LABEL) {
                v = (TextView) mInflater.inflate(R.layout.view_calendar_weekday,
                        parent, false);
                v.setTag(VIEW_LABEL);
            }

            v.setText(WEEK[FIRST_DAY_OF_WEEK][position]);
            v.setClickable(false);

        } else { // days of month

            if (v == null || tag != VIEW_VALUE) {
                v = (TextView) mInflater.inflate(R.layout.view_calendar_day,
                        parent, false);
                v.setTag(VIEW_VALUE);
            }

            // Calendar with current day
            Calendar c = mDates.get(position - 7);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DATE);

            // print the day of month
            v.setText("" + day);

            // days of current month with events
            int[] daysWithEvents = mEventsMap.get(month + 1);
            boolean hasEvents = daysWithEvents != null && daysWithEvents.length > 0;

            // if current date is outside of current month
            if (month != mSelectedMonth.get(Calendar.MONTH) || year != mSelectedMonth.get(Calendar.YEAR)) {
                v.setBackgroundColor(mBackgroundWithoutEvent);
                v.setTextColor(mTextcolorOutMonth);
                v.setClickable(true);

                // if there are events on current date
            } else if (hasEvents && Arrays.binarySearch(daysWithEvents, c.get(Calendar.DAY_OF_MONTH)) >= 0) {
                v.setBackgroundColor(mBackgroundWithEvent);
                v.setTextColor(mTextcolorInMonth);
                v.setClickable(false);

                // current date is on current month, but without events
            } else {
                v.setBackgroundColor(mBackgroundWithoutEvent);
                v.setTextColor(mTextcolorInMonth);
                v.setClickable(true);
            }
        }

        return v;
    }

}
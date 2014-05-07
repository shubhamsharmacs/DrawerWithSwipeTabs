package com.arcasolutions.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;
import com.weedfinder.R;
import com.arcasolutions.api.model.CheckIn;

import java.util.LinkedList;

public class CheckInAdapter extends ArrayAdapter<CheckIn> {

    public CheckInAdapter(Context context, LinkedList<CheckIn> objects) {
        super(context, R.layout.simple_list_item_checkin, R.id.checkInName, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        CheckIn checkIn = getItem(position);

        AQuery aq = new AQuery(view);
        aq.id(R.id.checkInImage).image(checkIn.getAccountImageUrl(), true, true);
        aq.id(R.id.checkInName).text(checkIn.getAccountName());
        aq.id(R.id.checkInDate).text(String.format("%tD", checkIn.getAdded()));
        aq.id(R.id.checkInQuickTip).text(checkIn.getQuickTip());

        return view;
    }
}

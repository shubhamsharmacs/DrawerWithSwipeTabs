package com.arcasolutions.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.arcasolutions.R;

public class DescriptionFragment extends Fragment {

    public static final String ARG_TEXT = "text";

    public DescriptionFragment() {}

    public static DescriptionFragment newInstance(CharSequence text) {
        final Bundle args = new Bundle();
        args.putCharSequence(ARG_TEXT, text);

        final DescriptionFragment f = new DescriptionFragment();
        f.setArguments(args);
        return f;
    }

    public CharSequence getShownText() {
        return getArguments() != null
                ? getArguments().getCharSequence(ARG_TEXT)
                : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_description, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        AQuery aq = new AQuery(view);
        aq.id(R.id.text1).text(getShownText());
    }
}

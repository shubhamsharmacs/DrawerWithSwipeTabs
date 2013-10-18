package com.arcasolutions.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.arcasolutions.R;

public class DescriptionFragment extends Fragment {

    public static final String ARG_TITLE = "title";
    public static final String ARG_CONTENT = "content";

    public DescriptionFragment() {
    }

    public static DescriptionFragment newInstance(CharSequence title, CharSequence content) {
        final Bundle args = new Bundle();
        args.putCharSequence(ARG_TITLE, title);
        args.putCharSequence(ARG_CONTENT, content);

        final DescriptionFragment f = new DescriptionFragment();
        f.setArguments(args);
        return f;
    }

    public CharSequence getShownTitle() {
        return getArguments() != null
                ? getArguments().getCharSequence(ARG_TITLE)
                : "";
    }

    public CharSequence getShownContent() {
        return getArguments() != null
                ? getArguments().getCharSequence(ARG_CONTENT)
                : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_description, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        AQuery aq = new AQuery(view);
        aq.id(R.id.text1).text(getShownTitle());
        aq.id(R.id.text2).text(getShownContent());
    }
}

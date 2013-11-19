package com.arcasolutions.ui.fragment.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.google.common.collect.Lists;

import java.util.List;

public class MyMapFilterFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String ARG_FILTER_MODULE_INDEX = "module_class";

    private final Filter mFilter = new Filter();

    private EditText mKeywordView;
    private EditText mLocationView;
    private FilterAdapter mFilterAdapter;

    public MyMapFilterFragment() {}

    public static MyMapFilterFragment newInstance(int moduleIndex) {
        final Bundle args = new Bundle();
        args.putInt(ARG_FILTER_MODULE_INDEX, moduleIndex);

        final MyMapFilterFragment f = new MyMapFilterFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        int sectionIndex = getArguments() != null
                ? getArguments().getInt(ARG_FILTER_MODULE_INDEX)
                : null;
        mFilter.setModuleIndex(sectionIndex);
        mFilterAdapter = new FilterAdapter(getActivity(), mFilter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.map, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_filter, container, false);
        mKeywordView = (EditText) view.findViewById(R.id.keyword);
        mLocationView = (EditText) view.findViewById(R.id.location);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(mFilterAdapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    private void chooseRatingDialog() {
        final RatingListAdapter adapter = new RatingListAdapter(getActivity(), mFilter);
        new AlertDialog.Builder(getActivity())
                .setTitle("Rating")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        List<Float> ratings = mFilter.getRatings();
                        if (ratings == null) ratings = Lists.newArrayList();

                        float rating = (float) i;

                        if (ratings.contains(rating)) {
                            ratings.remove(rating);
                        } else {
                            ratings.add(rating);
                        }

                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }

    private void chooseModuleDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.section)
                .setSingleChoiceItems(R.array.modules, mFilter.getModuleIndex(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mFilter.setModuleIndex(i);
                        mFilterAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create().show();
    }

    public Filter getFilter() {
        mFilter.setKeyword(mKeywordView.getText().toString());
        mFilter.setLocation(mLocationView.getText().toString());
        return mFilter;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0)
            chooseModuleDialog();

        else if (i == 1)
            chooseRatingDialog();
    }

    static class FilterAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;
        private final Filter mFilter;
        private final Resources mResources;

        FilterAdapter(Context context, Filter filter) {
            mInflater = LayoutInflater.from(context);
            mFilter = filter;
            mResources = context.getResources();
        }

        @Override
        public int getCount() {
            switch (mFilter.getModuleIndex()) {
                case Filter.ModuleIndex.LISTING:
                case Filter.ModuleIndex.DEAL:
                    return 2;

                default:
                    return 1;
            }
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            if (v == null) {
                v = mInflater.inflate(R.layout.simple_list_item_mapfilter, null);
            }
            AQuery aq = new AQuery(v);
            switch (i) {
                case 0:
                    aq.id(android.R.id.text1).text("Section");
                    aq.id(android.R.id.text2).text(mResources.getStringArray(R.array.modules)[mFilter.getModuleIndex()]);
                    break;

                case 1:
                    aq.id(android.R.id.text1).text("Rating");
                    aq.id(android.R.id.text2).text(
                            mFilter.getRatings() == null || mFilter.getRatings().isEmpty()
                                ? "Any"
                                : TextUtils.join(", ", mFilter.getRatings()) + " stars"
                    );
                    break;
            }

            return v;
        }
    }

    static class RatingListAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;
        private final Filter mFilter;

        RatingListAdapter(Context context, Filter filter) {
            mInflater = LayoutInflater.from(context);
            mFilter = filter;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Float getItem(int i) {
            return (float)getCount() - i;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (i == 0) {
                View labelView = mInflater.inflate(R.layout.checked_list_item_label, null);
                AQuery aq = new AQuery(labelView);
                aq.id(android.R.id.text1).text("Any");
                aq.id(R.id.checkBox).checked(mFilter.getRatings() == null
                        || mFilter.getRatings().isEmpty());
                return labelView;
            } else {
                View labelView = mInflater.inflate(R.layout.checked_list_item_rating, null);
                AQuery aq = new AQuery(labelView);
                aq.id(R.id.ratingBar).rating(getItem(i));
                aq.id(R.id.checkBox).checked(mFilter.getRatings() != null
                        && mFilter.getRatings().contains(i));
                return labelView;
            }
        }
    }

}

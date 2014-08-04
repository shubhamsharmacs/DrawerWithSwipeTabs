package com.arcasolutions.ui.fragment.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;

import com.androidquery.AQuery;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.BaseCategoryResult;
import com.arcasolutions.api.model.ListingCategoryResult;
import com.arcasolutions.ui.adapter.CategoryResultAdapter;
import com.arcasolutions.util.EmptyListViewHelper;
import com.weedfinder.R;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class MyMapFilterFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String ARG_FILTER_MODULE_INDEX = "module_class";
    private final List<BaseCategory> mCategories = Lists.newArrayList();
    private CategoryResultAdapter mAdapter;

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

        int sectionIndex = getArguments() != null
                ? getArguments().getInt(ARG_FILTER_MODULE_INDEX)
                : null;

        mFilter.setModuleIndex(sectionIndex);

        mFilterAdapter = new FilterAdapter(getActivity(), mFilter, mCategories);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_filter, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mKeywordView = (EditText) view.findViewById(R.id.keyword);
        mLocationView = (EditText) view.findViewById(R.id.location);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(mFilterAdapter);
        listView.setOnItemClickListener(this);
        displayProgress(true);
        loadCategories();
    }


   private void loadCategories() {

        Client.RestListener<BaseCategoryResult> mListener = new Client.RestListener<BaseCategoryResult>() {

            @Override
            public void onComplete(BaseCategoryResult result) {
                List<BaseCategory> categories = result.getResults();
                if (categories != null) {
                    mCategories.clear();
                    mCategories.addAll(categories);
                    mFilter.setCategory(String.valueOf(mCategories.get(0).getId()));
                    mFilterAdapter.notifyDataSetChanged();
                    displayProgress(false);
                }
            }

            @Override
            public void onFail(Exception ex) {
                ex.printStackTrace();
                displayProgress(false);
            }
        };

        Client.Builder builder = new Client.Builder(ListingCategoryResult.class);
        builder.filterMap();

        builder.execAsync(mListener);

    }


    private void displayProgress(boolean display) {
        if (getActivity() != null) {
            getActivity().setProgressBarIndeterminateVisibility(display);
        }
    }

    private void chooseRatingDialog() {
        final RatingListAdapter adapter = new RatingListAdapter(getActivity());
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.rating)
                .setAdapter(adapter, null)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, null).create();

        final ListView listView = dialog.getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                View currentView = listView.getChildAt(position);
                CheckBox checkBox = (CheckBox) currentView.findViewById(R.id.checkBox);
                checkBox.setChecked(!checkBox.isChecked());


                if (position == 0) {
                    if (checkBox.isChecked()) {
                        for (int i=1; i<listView.getChildCount(); i++) {
                            View c = listView.getChildAt(i);
                            CheckBox box = (CheckBox) c.findViewById(R.id.checkBox);
                            box.setChecked(false);
                        }
                    }
                }

                boolean thereIsOptionsChecked = false;
                for (int i=1; i<listView.getChildCount(); i++) {
                    View c = listView.getChildAt(i);
                    CheckBox box = (CheckBox) c.findViewById(R.id.checkBox);
                    if (box.isChecked()) {
                        thereIsOptionsChecked = true;
                        break;
                    }
                }
                View m = listView.getChildAt(0);
                CheckBox mbox = (CheckBox) m.findViewById(R.id.checkBox);
                mbox.setChecked(!thereIsOptionsChecked);

            }
        });


        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                listView.performItemClick(listView.getChildAt(0), 0, 0);
            }
        });

        dialog.show();

        Button okButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> ratings = Lists.newArrayList();
                for (int i=0; i<listView.getChildCount(); i++) {
                    View childView = listView.getChildAt(i);
                    CheckBox checkBox = (CheckBox) childView.findViewById(R.id.checkBox);
                    RatingBar ratingBar = (RatingBar) childView.findViewById(R.id.ratingBar);
                    if (i == 0 && checkBox.isChecked()) {
                        break;
                    } else if (checkBox.isChecked()) {
                        ratings.add((int)ratingBar.getRating());
                    }
                }
                mFilter.setRatings(ratings);
                mFilterAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

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

    private void chooseCategory() {

        List<String> list = Lists.newArrayList();
        for (int i=0; i<mCategories.size(); i++) {
            list.add(mCategories.get(i).getName());
        }

        CharSequence[] cs = list.toArray(new CharSequence[list.size()]);

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.category_title)

                .setSingleChoiceItems(cs, mFilter.getCategoryIndex(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mFilter.setCategory(String.valueOf(mCategories.get(i).getId()));
                        mFilter.setCategoryIndex(i);
                        mFilterAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                })
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

        else if (i == 2)
            chooseCategory();

    }



    static class FilterAdapter extends BaseAdapter {

        private final Context mContext;
        private final LayoutInflater mInflater;
        private final Filter mFilter;
        private final Resources mResources;
        private final List<? extends BaseCategory> mCategoriesAdapter;

        FilterAdapter(Context context, Filter filter, List<? extends BaseCategory> categories) {
            mInflater = LayoutInflater.from(context);
            mFilter = filter;
            mResources = context.getResources();
            mContext = context;
            mCategoriesAdapter = categories;
        }

        @Override
        public int getCount() {
            switch (mFilter.getModuleIndex()) {
                case Filter.ModuleIndex.LISTING:
                    return 3;

                case Filter.ModuleIndex.DEAL:
                    mFilter.setCategory(null);
                    return 2;

                default:
                    mFilter.setCategory(null);
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
                    aq.id(android.R.id.text1).text(R.string.section);
                    aq.id(android.R.id.text2).text(mResources.getStringArray(R.array.modules)[mFilter.getModuleIndex()]);
                    break;

                case 1:
                    String starsLabel = mContext.getString(R.string.any);
                    List<Integer> ratings = mFilter.getRatings();
                    int size = ratings != null
                            ? ratings.size()
                            : 0;

                    if (size > 0) {
                        starsLabel = "";
                        for (int j=0; j<size; j++) {
                            starsLabel += ratings.get(j);
                            if (size > 1 && j == (size - 2)) starsLabel += " & ";
                            else if (j < size - 2) starsLabel += ", ";
                        }
                        starsLabel += " " + mContext.getString(R.string.stars);
                    }

                    aq.id(android.R.id.text1).text(R.string.rating);
                    aq.id(android.R.id.text2).text(starsLabel);
                    break;

                case 2:
                    aq.id(android.R.id.text1).text(R.string.categories);
                    if (mCategoriesAdapter.size() > 0) {
                        aq.id(android.R.id.text2).text(mCategoriesAdapter.get(mFilter.getCategoryIndex()).getName());
                    }

                    break;
            }

            return v;
        }
    }

    static class RatingListAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;

        RatingListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
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
                aq.id(android.R.id.text1).text(R.string.any);
                return labelView;
            } else {
                View labelView = mInflater.inflate(R.layout.checked_list_item_rating, null);
                AQuery aq = new AQuery(labelView);
                aq.id(R.id.ratingBar).rating(getItem(i));
                return labelView;
            }
        }
    }

}

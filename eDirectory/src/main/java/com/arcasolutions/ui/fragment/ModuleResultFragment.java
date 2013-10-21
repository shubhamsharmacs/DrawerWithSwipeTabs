package com.arcasolutions.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.adapter.ModuleResultAdapter;
import com.arcasolutions.util.AbsListViewHelper;
import com.google.common.collect.Lists;

import java.util.List;

public class ModuleResultFragment<T extends BaseResult>
        extends Fragment
        implements
            Client.RestListener<T>, AdapterView.OnItemClickListener,
            AbsListViewHelper.OnNextPageListener {


    public interface OnModuleSelectionListener {
        void onModuleSelected(Module module, int position, long id);
    }

    public static final String ARG_ITEMS = "items";
    public static final String ARG_CATEGORY = "category";
    public static final String ARG_TYPE = "type";

    // arguments
    private BaseCategory mCatagory;
    private int mPage = 1;
    private Class<T> mType;

    private final List<Module> mModules = Lists.newArrayList();
    private ModuleResultAdapter mAdapter;

    private OnModuleSelectionListener mListener;

    private enum SortOpt {
        RATING, ALPHABETICALLY, DISTANCE, RECENTLY_ADDED;
    }

    private SortOpt mSortOpt;

    private TextView mHeader;

    private AbsListViewHelper mListViewHelper;

    public ModuleResultFragment() {
    }

    public static <F extends BaseResult> ModuleResultFragment<F> newInstance(Class<F> clazz, BaseCategory category) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_CATEGORY, category);
        args.putSerializable(ARG_TYPE, clazz);
        final ModuleResultFragment<F> f = new ModuleResultFragment<F>();
        f.setArguments(args);
        return f;
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
        mAdapter = new ModuleResultAdapter(getActivity(), mModules);

        setHasOptionsMenu(true);

        Bundle args = getArguments();
        if (args != null) {
            mType = (Class<T>) args.getSerializable(ARG_TYPE);
            mCatagory = args.getParcelable(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.custom_list_view, container, false);
        mHeader = (TextView) rootView.findViewById(android.R.id.text1);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        mListViewHelper = new AbsListViewHelper(listView, this);
        listView.setOnItemClickListener(this);
        listView.setAdapter(mAdapter);
        loadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.results, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_rating:
                item.setChecked(true);
                changeOrder(SortOpt.RATING);
                break;

            case R.id.action_alphabetically:
                item.setChecked(true);
                changeOrder(SortOpt.ALPHABETICALLY);
                break;

            case R.id.action_distance:
                item.setChecked(true);
                changeOrder(SortOpt.DISTANCE);
                break;

            case R.id.action_recently_added:
                item.setChecked(true);
                changeOrder(SortOpt.RECENTLY_ADDED);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(T result) {
        mListViewHelper.finishLoading();
        mListViewHelper.changeBaseResult(result);

        List results = result.getResults();
        if (results != null) {
            mModules.addAll(results);
            mAdapter.notifyDataSetChanged();
        }
        if (!TextUtils.isEmpty(result.getError())) {
            displayError();
        }
    }

    @Override
    public void onFail(Exception ex) {
        mListViewHelper.finishLoading();
        displayError();
    }

    private void displayError() {
        AQuery aq = new AQuery(getView());
        aq.id(android.R.id.list).getListView()
                .setEmptyView(aq.id(R.id.error).getView());
    }

    private void changeOrder(SortOpt sortOpt) {
        mSortOpt = sortOpt;
        mModules.clear();
        mAdapter.notifyDataSetChanged();
        loadData();
    }

    private void loadData() {
        new AQuery(getView()).id(android.R.id.list).getListView().setEmptyView(new ProgressBar(getActivity()));

        Client.Builder builder = new Client.Builder(mType);
        builder.page(mPage);

        if (mCatagory != null) {
            builder.categoryId(mCatagory.getId());
            builder.searchBy(SearchBy.CATEGORY);
        }

        if (mSortOpt != null) {
            AQuery aq = new AQuery(mHeader);
            switch (mSortOpt) {

                case RATING:
                    aq.id(android.R.id.text1).text(getString(R.string.show_results_by_sort, getString(R.string.rating)))
                            .visible();
                    builder.orderBy("rate");
                    builder.orderSequence("desc");
                    break;

                case ALPHABETICALLY:
                    aq.id(android.R.id.text1).text("Showing results by \"alphabetical\"")
                            .visible();
                    builder.orderBy("name");
                    builder.orderSequence("asc");
                    break;

                case DISTANCE:
                    aq.id(android.R.id.text1).text("Showing results by \"distance\"")
                            .visible();
                    builder.orderBy("distance_score");
                    builder.orderSequence("asc");
                    break;

                case RECENTLY_ADDED:
                    aq.id(android.R.id.text1).text("Showing results by \"recently added\"")
                            .visible();
                    builder.orderBy("datecreated");
                    builder.orderSequence("asc");
                    break;
            }
        }

        mListViewHelper.startLoading();
        builder.execAsync(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mListener != null) {
            Module m = (Module) adapterView.getItemAtPosition(i);
            mListener.onModuleSelected(m, i, l);
        }
    }

    @Override
    public void onNextPage() {
        mPage += 1;
        loadData();
    }

}

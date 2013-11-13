package com.arcasolutions.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.adapter.ModuleResultAdapter;
import com.arcasolutions.util.AbsListViewHelper;
import com.arcasolutions.util.EmptyListViewHelper;
import com.arcasolutions.util.Util;
import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.List;

public class ModuleResultFragment<T extends BaseResult>
        extends Fragment
        implements  Client.RestListener<T>, AdapterView.OnItemClickListener,
            AbsListViewHelper.OnNextPageListener {

    public static final String ARG_CATEGORY = "category";
    public static final String ARG_TYPE = "type";

    // arguments
    private BaseCategory mCatagory;
    private int mPage = 1;
    private Class<T> mType;

    private final LinkedList<Module> mModules = new LinkedList<Module>();
    private ListView mListView;
    private ModuleResultAdapter mAdapter;

    private OnModuleSelectionListener mListener;

    private OrderBy mOrderBy;

    private AbsListViewHelper mListViewHelper;
    private MenuItem mOrderItem;

    private EmptyListViewHelper mEmptyHelper;

    // Default constructor
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
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListViewHelper = new AbsListViewHelper(mListView, this);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
        mEmptyHelper = new EmptyListViewHelper(mListView, R.drawable.no_results, R.string.your_search_matched_no_results);
        loadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.results, menu);
        mOrderItem = menu.findItem(R.id.action_sort);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_level:
                changeOrder(OrderBy.LEVEL);
                break;

            case R.id.action_alphabetically:
                changeOrder(OrderBy.ALPHABETICALLY);
                break;

            case R.id.action_popular:
                changeOrder(OrderBy.POPULAR);
                break;

            case R.id.action_rating:
                changeOrder(OrderBy.RATING);
                break;

            case R.id.action_distance:
                changeOrder(OrderBy.DISTANCE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(T result) {
        mListViewHelper.finishLoading();
        mListViewHelper.changeBaseResult(result);

        List results = result.getResults();
        int size = results != null ? results.size() : 0;
        if (size > 0) {
            mModules.addAll(results);
            mAdapter.notifyDataSetChanged();
        } else {
            mEmptyHelper.empty();
        }
    }

    @Override
    public void onFail(Exception ex) {
        mListViewHelper.finishLoading();
        mEmptyHelper.error();
    }

    private void changeOrder(OrderBy sortOpt) {
        mOrderBy = sortOpt;
        mModules.clear();
        mAdapter.notifyDataSetChanged();
        loadData();
    }

    private void loadData() {

        Client.Builder builder = new Client.Builder(mType);
        builder.page(mPage);

        if (mCatagory != null) {
            builder.categoryId(mCatagory.getId());
            builder.searchBy(SearchBy.CATEGORY);
        }

        if (mOrderBy != null) {
            CharSequence headerInfo = null;
            switch (mOrderBy) {

                case LEVEL:
                    headerInfo = getString(R.string.show_results_by_sort, getString(R.string.level));
                    builder.orderBy("level,name");
                    builder.orderSequence("asc,asc");
                    break;

                case ALPHABETICALLY:
                    headerInfo = getString(R.string.show_results_by_sort, getString(R.string.alphabetically));
                    builder.orderBy("name");
                    builder.orderSequence("asc");
                    break;

                case POPULAR:
                    headerInfo = getString(R.string.show_results_by_sort, getString(R.string.popular));
                    builder.orderBy("name");
                    builder.orderSequence("asc");
                    break;

                case RATING:
                    headerInfo = getString(R.string.show_results_by_sort, getString(R.string.rating));
                    builder.orderBy("rate");
                    builder.orderSequence("desc");
                    break;

                case DISTANCE:
                    headerInfo = getString(R.string.show_results_by_sort, getString(R.string.distance));
                    builder.orderBy("distance_score");
                    builder.myLocation(Util.getMyLocation().getLatitude(), Util.getMyLocation().getLongitude());
                    builder.orderSequence("asc");
                    break;

            }
        }

        mEmptyHelper.progress();
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

    // Module selection listener
    public interface OnModuleSelectionListener {
        void onModuleSelected(Module module, int position, long id);
    }

    // OrderBy options
    private enum OrderBy {
        LEVEL, ALPHABETICALLY, POPULAR, RATING, DISTANCE;
    }


}

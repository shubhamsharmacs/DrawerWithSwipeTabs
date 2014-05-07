package com.arcasolutions.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.weedfinder.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.OrderBy;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.ArticleResult;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.ClassifiedResult;
import com.arcasolutions.api.model.DealResult;
import com.arcasolutions.api.model.EventResult;
import com.arcasolutions.api.model.ListingResult;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.OnModuleSelectionListener;
import com.arcasolutions.ui.adapter.ModuleResultAdapter;
import com.arcasolutions.util.AbsListViewHelper;
import com.arcasolutions.util.EmptyListViewHelper;
import com.arcasolutions.util.Util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModuleResultFragment<T extends BaseResult>
        extends Fragment
        implements Client.RestListener<T>, AdapterView.OnItemClickListener,
        AbsListViewHelper.OnNextPageListener {

    public static final String ARG_CATEGORY = "category";
    public static final String ARG_TYPE = "type";
    public static final String ARG_RESULTS = "results";

    private static final String TAG = ModuleResultFragment.class.getSimpleName();

    // arguments
    private BaseCategory mCatagory;
    private int mPage = 1;
    private Class<T> mType;

    private final LinkedList<Module> mModules = new LinkedList<Module>();
    private ModuleResultAdapter mAdapter;

    private OnModuleSelectionListener mListener;

    private OrderBy mOrderBy;

    private AbsListViewHelper mListViewHelper;
    private MenuItem mOrderByItem;

    private EmptyListViewHelper mEmptyHelper;
    private boolean mIsFixedResults = false;

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

    public static <F extends BaseResult> ModuleResultFragment<F> newInstance(Class<F> clazz, ArrayList<Module> items) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE, clazz);
        args.putParcelableArrayList(ARG_RESULTS, items);
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
            List<Module> results = args.getParcelableArrayList(ARG_RESULTS);
            if (results != null) {
                mModules.addAll(results);
                mIsFixedResults = true;
            }
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
        ListView mListView = (ListView) view.findViewById(android.R.id.list);
        mListViewHelper = new AbsListViewHelper(mListView, this);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
        mEmptyHelper = new EmptyListViewHelper(mListView, R.drawable.no_results, R.string.your_search_matched_no_results);
        loadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (mIsFixedResults) return;

        if (ArticleResult.class.equals(mType)) {
            inflater.inflate(R.menu.results_article, menu);

        } else if (ClassifiedResult.class.equals(mType)) {
            inflater.inflate(R.menu.results_classified, menu);

        } else if (DealResult.class.equals(mType)) {
            inflater.inflate(R.menu.results_deal, menu);

        } else if (EventResult.class.equals(mType)) {
            inflater.inflate(R.menu.results_event, menu);

        } else if (ListingResult.class.equals(mType)) {
            inflater.inflate(R.menu.results_listing, menu);

        }

        mOrderByItem = menu.findItem(R.id.action_order_by);
        mOrderByItem.setVisible(!mModules.isEmpty());
        setMenuOrderEnable(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_end_date:
                changeOrder(OrderBy.END_DATE);
                break;

            case R.id.action_last_update:
                changeOrder(OrderBy.LAST_UPDATED);
                break;

            case R.id.action_publication_date:
                changeOrder(OrderBy.PUBLICATION_DATE);
                break;

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
            if (mOrderByItem != null) mOrderByItem.setVisible(true);
            mModules.addAll(results);
            if (OrderBy.DISTANCE.equals(mOrderBy)) {
                Util.orderByDistance(mModules);
            }
            mAdapter.notifyDataSetChanged();
        } else {
            mEmptyHelper.empty();
        }

        setMenuOrderEnable(!mModules.isEmpty());
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
        if (mIsFixedResults) return;

        Client.Builder builder = new Client.Builder(mType);
        builder.page(mPage);

        if (mCatagory != null) {
            builder.categoryId(mCatagory.getId());
            builder.searchBy(SearchBy.CATEGORY);
        }

        if (mOrderBy != null) {

            try {
                T t = mType.newInstance();
                Map<String, String> orderMap = t.getOrderMap(mOrderBy);
                for (Map.Entry<String, String> entry : orderMap.entrySet()) {
                    builder.orderBy(entry.getKey());
                    builder.orderSequence(entry.getValue());
                }
            } catch (Exception ignored) {
                Log.e(TAG, ignored.getMessage(), ignored);
            }


            CharSequence headerInfo = null;
            switch (mOrderBy) {

                case START_DATE:
                    break;

                case END_DATE:

                    break;

                case PUBLICATION_DATE:
                    break;

                case LEVEL:
                    headerInfo = getString(R.string.show_results_by_sort, getString(R.string.level));
                    break;

                case ALPHABETICALLY:
                    headerInfo = getString(R.string.show_results_by_sort, getString(R.string.alphabetically));
                    break;

                case LAST_UPDATED:
                    headerInfo = getString(R.string.show_results_by_sort, getString(R.string.alphabetically));
                    break;

                case POPULAR:
                    headerInfo = getString(R.string.show_results_by_sort, getString(R.string.popular));
                    break;

                case RATING:
                    headerInfo = getString(R.string.show_results_by_sort, getString(R.string.rating));
                    break;

                case DISTANCE:
                    headerInfo = getString(R.string.show_results_by_sort, getString(R.string.distance));
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

    private void setMenuOrderEnable(boolean enabled) {
        if (mOrderByItem != null) {
            mOrderByItem.setEnabled(enabled);
        }
    }

}

package com.arcasolutions.ui.fragment;

import android.app.Activity;
import android.content.res.Configuration;
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

import com.arcasolutions.R;
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
import com.arcasolutions.ui.adapter.HomeResultAdapter;
import com.arcasolutions.ui.adapter.ModuleResultAdapter;
import com.arcasolutions.util.AbsListViewHelper;
import com.arcasolutions.util.EmptyListViewHelper;
import com.arcasolutions.util.Util;
import com.origamilabs.library.views.StaggeredGridView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HomeResultFragment<T extends BaseResult>
        extends Fragment implements
            Client.RestListener<T>,
            StaggeredGridView.OnItemClickListener,
            StaggeredGridView.OnLoadmoreListener {

    public static final String ARG_TYPE = "type";

    private static final String TAG = HomeResultFragment.class.getSimpleName();

    // arguments
    private int mPage = 1;
    private Class<T> mType;

    private final LinkedList<Module> mModules = new LinkedList<Module>();
    private HomeResultAdapter mAdapter;

    private OnModuleSelectionListener mListener;

    private StaggeredGridView mGridView;

    // Default constructor
    public HomeResultFragment() {
    }

    public static <F extends BaseResult> HomeResultFragment<F> newInstance(Class<F> clazz) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE, clazz);
        final HomeResultFragment<F> f = new HomeResultFragment<F>();
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
        mAdapter = new HomeResultAdapter(getActivity(), mModules);

        setHasOptionsMenu(true);

        Bundle args = getArguments();
        if (args != null) {
            mType = (Class<T>) args.getSerializable(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mGridView = (StaggeredGridView) rootView.findViewById(R.id.grid);
        mGridView.setSelector(R.drawable.abc_list_selector_holo_light);
        mGridView.setDrawSelectorOnTop(true);
        mGridView.setOnItemClickListener(this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnLoadmoreListener(this);
        mGridView.setItemMargin(getResources().getDimensionPixelSize(R.dimen.spacingSmall));
        loadData();
        setupGridColumnsCount(getResources().getConfiguration());
        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupGridColumnsCount(newConfig);
    }

    private void setupGridColumnsCount(Configuration configuration) {
        if (mGridView == null || configuration == null) return;

        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridView.setColumnCount(3);
        } else {
            mGridView.setColumnCount(2);
        }
        mGridView.requestLayout();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(T result) {

        List results = result.getResults();
        int size = results != null ? results.size() : 0;
        if (size > 0) {
            mModules.addAll(results);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onFail(Exception ex) {
//        mEmptyHelper.error();
    }



    private void loadData() {

        Client.Builder builder = new Client.Builder(mType);
        builder.page(mPage);

//        if (mCatagory != null) {
//            builder.categoryId(mCatagory.getId());
//            builder.searchBy(SearchBy.CATEGORY);
//        }

//        mEmptyHelper.progress();
        builder.execAsync(this);
    }


    @Override
    public void onItemClick(StaggeredGridView parent, View view, int position, long id) {
        if (mListener != null) {
            Module m = (Module) mAdapter.getItem(position);
            mListener.onModuleSelected(m, position, id);
        }
    }

    @Override
    public void onLoadmore() {
        mPage += 1;
        loadData();
    }

    // Module selection listener
    public interface OnModuleSelectionListener {
        void onModuleSelected(Module module, int position, long id);
    }


}

package com.arcasolutions.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.BaseCategoryResult;
import com.arcasolutions.ui.adapter.CategoryResultAdapter;
import com.arcasolutions.util.EmptyListViewHelper;
import com.google.common.collect.Lists;

import java.util.List;

public class CategoryResultFragment extends Fragment
        implements AdapterView.OnItemClickListener {

    public static final String ARG_TYPE = "type";
    public static final String ARG_CATEGORY = "category";

    private CategoryResultAdapter mAdapter;
    private final List<BaseCategory> mCategories = Lists.newArrayList();
    private OnCategorySelectionListener mListener;
    private EmptyListViewHelper mEmptyListViewHelper;

    public interface OnCategorySelectionListener {
        void onCategorySelected(BaseCategory category);
    }

    public CategoryResultFragment() {
    }

    public static CategoryResultFragment newInstance(Class<? extends BaseCategoryResult> clazz) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE, clazz);
        final CategoryResultFragment f = new CategoryResultFragment();
        f.setArguments(args);
        return f;
    }

    public static CategoryResultFragment newInstance(Class<? extends BaseCategoryResult> clazz, BaseCategory category) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE, clazz);
        args.putParcelable(ARG_CATEGORY, category);
        final CategoryResultFragment f = new CategoryResultFragment();
        f.setArguments(args);
        return f;
    }

    public Class getShownClass() {
        return getArguments() != null
                ? (Class) getArguments().getSerializable(ARG_TYPE)
                : null;
    }

    public BaseCategory getShownCategory() {
        return getArguments() != null
                ? (BaseCategory) getArguments().getParcelable(ARG_CATEGORY)
                : null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnCategorySelectionListener) {
            mListener = (OnCategorySelectionListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new CategoryResultAdapter(getActivity(), mCategories);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup mRootView = (ViewGroup) inflater.inflate(R.layout.simple_list_view, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
        mEmptyListViewHelper = new EmptyListViewHelper(mListView, R.drawable.no_results);
        loadCategories();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mListener != null) {
            BaseCategory category = mAdapter.getItem(i);
            mListener.onCategorySelected(category);
        }
    }

    private void loadCategories() {

        if (!mCategories.isEmpty()) return;

        Client.RestListener<BaseCategoryResult> mListener = new Client.RestListener<BaseCategoryResult>() {

            @Override
            public void onComplete(BaseCategoryResult result) {
                List<BaseCategory> categories = result.getResults();
                if (categories != null) {
                    mCategories.addAll(categories);
                    mAdapter.notifyDataSetChanged();
                }
                mEmptyListViewHelper.empty();
            }

            @Override
            public void onFail(Exception ex) {
                ex.printStackTrace();
                mEmptyListViewHelper.error();
            }
        };

        Client.Builder builder = new Client.Builder(getShownClass());
        BaseCategory category = getShownCategory();
        if (category != null) {
            builder.fatherId(category.getId());
        }

        builder.execAsync(mListener);
        mEmptyListViewHelper.progress();
    }

}

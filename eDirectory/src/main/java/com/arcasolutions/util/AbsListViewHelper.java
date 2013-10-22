package com.arcasolutions.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.arcasolutions.R;
import com.arcasolutions.api.model.BaseResult;

public class AbsListViewHelper implements AbsListView.OnScrollListener {

    private int mTotalResults = Integer.MIN_VALUE;

    private boolean mLoading = false;

    private final ViewGroup mContainer;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private View mFooterView;

    private OnNextPageListener mListener;
    private boolean mIsLast = false;


    public AbsListViewHelper(ListView listView, OnNextPageListener listener) {
        if (listView == null)
            throw new IllegalArgumentException("AbsListView must not be null");

        if (listener == null)
            throw new IllegalArgumentException("OnNextPageListener must not be null");

        Context context = listView.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        mProgressBar = new ProgressBar(context);
        mProgressBar.setId(android.R.id.empty);

        mListener = listener;
        mListView = listView;

        mContainer = (ViewGroup) mListView.getParent();

        mFooterView = inflater.inflate(R.layout.simple_list_footer_loading, null);
        mFooterView.setVisibility(View.GONE);
        mListView.setFooterDividersEnabled(true);
        mListView.addFooterView(mFooterView, null, false);
        mListView.setOnScrollListener(this);
    }

    public void changeBaseResult(BaseResult baseResult) {
        if (baseResult != null) {
            mTotalResults = baseResult.getTotalResults();
        }
    }

    public void startLoading() {
        mLoading = true;
        mFooterView.setPadding(0, 0, 0, 0);
        mFooterView.setVisibility(View.VISIBLE);
        if (mContainer != null) mContainer.addView(mProgressBar);
        mListView.setEmptyView(mProgressBar);
    }

    public void finishLoading() {
        mLoading = false;
        mFooterView.setPadding(0, -1000, 0, 0);
        mFooterView.setVisibility(View.GONE);
        if (mContainer != null) mContainer.removeView(mProgressBar);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mLoading) return;

        int headerCount = mListView.getFooterViewsCount();
        int footerCount = mListView.getFooterViewsCount();
        int itemCount = mListView.getCount();

        boolean hasResults = (headerCount + footerCount + itemCount) < mTotalResults;

        if (hasResults && mIsLast && scrollState == SCROLL_STATE_IDLE) {
            mListener.onNextPage();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mIsLast = firstVisibleItem + visibleItemCount == totalItemCount;
    }

    public interface OnNextPageListener {
        void onNextPage();
    }
}

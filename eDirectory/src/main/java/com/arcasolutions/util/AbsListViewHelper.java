package com.arcasolutions.util;

import android.widget.AbsListView;

import com.arcasolutions.api.model.BaseResult;

public class AbsListViewHelper implements AbsListView.OnScrollListener {

    private int mTotalResults = Integer.MIN_VALUE;
    private AbsListView mListView;
    private OnNextPageListener mListener;

    private boolean mLoading = false;


    public AbsListViewHelper(AbsListView listView, OnNextPageListener listener) {
        if (listView == null)
            throw new IllegalArgumentException("AbsListView must not be null");

        if (listener == null)
            throw new IllegalArgumentException("OnNextPageListener must not be null");

        mListView = listView;
        mListener = listener;

        mListView.setOnScrollListener(this);
    }

    public void changeBaseResult(BaseResult baseResult) {
        if (baseResult != null) {
            mTotalResults = baseResult.getTotalResults();
        }
    }

    public void startLoading() {
        mLoading = true;
    }

    public void finishLoading() {
        mLoading = false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mLoading) return;

        boolean hasResults = view.getAdapter().getCount() < mTotalResults;

        if (hasResults && scrollState == SCROLL_STATE_IDLE) {
            mListener.onNextPage();
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public interface OnNextPageListener {
        void onNextPage();
    }
}

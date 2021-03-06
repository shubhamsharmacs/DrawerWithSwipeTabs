package com.arcasolutions.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.weedfinder.R;

public class EmptyListViewHelper {

    private Context mContext;
    private ListView mListView;
    private int mEmptyDrawableId;
    private int mEmptyStringId;

    public EmptyListViewHelper(ListView listView, int emptyDrawableId, int emptyStringId) {
        mListView = listView;
        mEmptyDrawableId = emptyDrawableId;
        mEmptyStringId = emptyStringId;
        mContext = mListView.getContext();
    }

    public EmptyListViewHelper(ListView listView, int emptyDrawableId) {
        this(listView, emptyDrawableId, 0);
    }

    public void empty() {
        ViewGroup parent = (ViewGroup) mListView.getParent();
        if (parent != null) {
            removeExistingEmptyView(parent);
            View emptyView = getEmptyView(mEmptyDrawableId, mEmptyStringId);
            parent.addView(emptyView);
            mListView.setEmptyView(emptyView);
        }
    }

    public void progress() {
        ViewGroup parent = (ViewGroup) mListView.getParent();
        if (parent != null) {
            removeExistingEmptyView(parent);
            View emptyView = getProgressBar();
            parent.addView(emptyView);
            mListView.setEmptyView(emptyView);
        }
    }

    public void error() {
        ViewGroup parent = (ViewGroup) mListView.getParent();
        if (parent != null) {
            removeExistingEmptyView(parent);
            int mOopsDrawableId = R.drawable.oops;
            int mOopsStringId = R.string.oops_something_went_wrong;
            View emptyView = getEmptyView(mOopsDrawableId, mOopsStringId);
            parent.addView(emptyView);
            mListView.setEmptyView(emptyView);
        }
    }

    private void removeExistingProgressView(ViewGroup parent) {
        View workingView = parent.findViewById(R.id.working);
        if (workingView != null) {
            parent.removeView(workingView);
        }
        if (parent.findViewById(R.id.working) != null) {
            removeExistingProgressView(parent);
        }
    }

    private void removeExistingEmptyView(ViewGroup parent) {
        removeExistingProgressView(parent);
        View existingEmptyView = parent.findViewById(android.R.id.empty);
        if (existingEmptyView != null) {
            parent.removeView(existingEmptyView);
        }
        if (parent.findViewById(android.R.id.empty) != null)
            removeExistingEmptyView(parent);
    }

    public View getProgressBar() {
        ProgressBar progressBar = new ProgressBar(mContext);
        progressBar.setId(R.id.working);
        progressBar.setVisibility(View.GONE);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(layoutParams);
        return progressBar;
    }

    private View getEmptyView(int drawableId, int stringId) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View emptyView = mInflater.inflate(R.layout.custom_empty_view, null);
        emptyView.setId(android.R.id.empty);
        emptyView.setVisibility(View.GONE);
        AQuery aq = new AQuery(emptyView);
        aq.id(R.id.image).image(drawableId);
        CharSequence text = stringId != 0
                ? mContext.getString(stringId)
                : null;
        aq.id(R.id.text1).text(text, true);
        return emptyView;
    }

}

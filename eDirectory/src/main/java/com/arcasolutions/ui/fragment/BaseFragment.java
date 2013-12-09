package com.arcasolutions.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.arcasolutions.R;

public abstract class BaseFragment extends Fragment {

    private ShareActionProvider mShareActionProvider;
    private MenuItem mShareMenuItem;
    private Intent mShareIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (!menuVisible) {
            finishProgress();
        }
    }

    protected void startProgress() {
        if (getActivity() == null) return;
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    protected void finishProgress() {
        if (getActivity() == null) return;
        getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_share, menu);

        mShareMenuItem = menu.findItem(R.id.action_share);
        if (mShareMenuItem != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(mShareMenuItem);
            mShareActionProvider.onCreateActionView();
            if (mShareIntent != null) mShareActionProvider.setShareIntent(mShareIntent);
            mShareMenuItem.setVisible(mShareIntent != null);
        }
    }

    protected void doShare(Intent shareIntent) {
        if (shareIntent == null) return;

        mShareIntent = shareIntent;

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }

        if (mShareMenuItem != null) {
            mShareMenuItem.setVisible(true);
        }
    }

}

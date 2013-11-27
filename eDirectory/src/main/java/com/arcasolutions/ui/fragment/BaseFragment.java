package com.arcasolutions.ui.fragment;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

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

}

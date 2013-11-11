package com.arcasolutions.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.arcasolutions.R;
import com.arcasolutions.api.model.Article;
import com.arcasolutions.api.model.Classified;
import com.arcasolutions.api.model.Deal;
import com.arcasolutions.api.model.Event;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.adapter.ModuleResultAdapter;
import com.arcasolutions.util.EmptyListViewHelper;
import com.arcasolutions.util.FavoriteHelper;
import com.google.common.collect.Lists;

import java.util.List;

public class ModuleFavoriteFragment<T extends Module> extends Fragment implements AdapterView.OnItemClickListener {

    public static final String ARG_CLASS = "class";

    private FavoriteHelper<T> mHelper;
    private Class<T> mClazz;
    private EmptyListViewHelper mEmptyListHelper;
    private final List<Module> mModules = Lists.newArrayList();
    private ModuleResultAdapter mAdapter;

    private OnModuleSelectionListener mListener;
    private ActionMode mActionMode;
    private ListView mListView;

    public ModuleFavoriteFragment() {}

    public static ModuleFavoriteFragment newInstance(Class<? extends Module> clazz) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_CLASS, clazz);

        final ModuleFavoriteFragment f = new ModuleFavoriteFragment();
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

        mClazz = getArguments() != null
                ? (Class<T>) getArguments().getSerializable(ARG_CLASS)
                : null;

        mHelper = new FavoriteHelper<T>(getActivity(), mClazz);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
        getActivity().registerReceiver(mFavoriteChangesReceiver, mFilter);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mFavoriteChangesReceiver);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
        mEmptyListHelper = new EmptyListViewHelper(mListView, getEmptyDrawableResId());
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (mActionMode == null) {
                    mActionMode = ((ActionBarActivity) getActivity()).startSupportActionMode(mActionModeCallback);
                }

                mListView.setItemChecked(position, true);
                return true;
            }
        });
    }

    private int getEmptyDrawableResId() {
        if (Listing.class.equals(mClazz))
            return R.drawable.no_business;
        else if (Article.class.equals(mClazz))
            return R.drawable.no_articles;
        else if (Event.class.equals(mClazz))
            return R.drawable.no_events;
        else if (Classified.class.equals(mClazz))
            return R.drawable.no_classifieds;
        else if (Deal.class.equals(mClazz))
            return R.drawable.no_deals;
        else
            return 0;
    }

    private void loadData() {
        List<T> modules = mHelper.getFavorites();
        mModules.clear();
        if (modules != null && !modules.isEmpty()) {
            mModules.addAll(modules);
            mAdapter.notifyDataSetChanged();
        } else {
            mEmptyListHelper.empty();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mListener != null) {
            Module m = (Module) adapterView.getItemAtPosition(i);
            mListener.onModuleSelected(m, i, l);
        }
    }

    // Module selection listener
    public interface OnModuleSelectionListener {
        void onModuleSelected(Module module, int position, long id);
    }

    private void deleteSelectedItems() {
        final long[] selectedIds = mListView.getCheckedItemIds();
        if (selectedIds != null) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Confirmation")
                    .setMessage("Confirm delete selected items?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            List<Long> ids = Lists.newArrayList();
                            for (long id : selectedIds) {
                                ids.add(id);
                            }
                            mHelper.destroy(ids);
                            if (mActionMode != null) {
                                mActionMode.finish();
                            }
                        }
                    })
                    .create().show();
        }
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.favorites, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deleteSelectedItems();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    private final IntentFilter mFilter = new IntentFilter(FavoriteHelper.ACTION_FAVORITE_CHANGED);

    private final BroadcastReceiver mFavoriteChangesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadData();
        }
    };

}

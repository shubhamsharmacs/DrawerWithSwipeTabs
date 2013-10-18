package com.arcasolutions.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.adapter.ModuleResultAdapter;
import com.google.common.collect.Lists;

import java.util.List;

public class ModuleResultFragment<T extends BaseResult>
        extends Fragment implements Client.RestListener<T>, AdapterView.OnItemClickListener {

    public interface OnModuleSelectionListener {
        void onModuleSelected(Module module, int position, long id);
    }

    public static final String ARG_ITEMS = "items";
    public static final String ARG_CATEGORY = "category";
    public static final String ARG_TYPE = "type";

    // arguments
    private BaseCategory mCatagory;
    private int mPage = 1;
    private Class<T> mType;

    private final List<Module> mModules = Lists.newArrayList();
    private ModuleResultAdapter mAdapter;

    private OnModuleSelectionListener mListener;

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

        Bundle args = getArguments();
        if (args != null) {
            mType = (Class<T>) args.getSerializable(ARG_TYPE);
            mCatagory = args.getParcelable(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AQuery aq = new AQuery(view);
        aq.id(android.R.id.list).itemClicked(this).adapter(mAdapter);
        loadData();
    }

    @Override
    public void onComplete(T result) {
        List results = result.getResults();
        if (results != null) {
            mModules.addAll(results);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFail(Exception ex) {

    }

    private void loadData() {
        Client.Builder builder = new Client.Builder(mType);
        builder.page(mPage);

        if (mCatagory != null) {
            builder.categoryId(mCatagory.getId());
            builder.searchBy(SearchBy.CATEGORY);
        }

        builder.execAsync(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mListener != null) {
            Module m = (Module) adapterView.getItemAtPosition(i);
            mListener.onModuleSelected(m, i, l);
        }
    }

}

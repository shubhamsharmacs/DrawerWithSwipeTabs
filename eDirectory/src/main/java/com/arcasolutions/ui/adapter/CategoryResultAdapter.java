package com.arcasolutions.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.DealCategory;

import java.util.List;

public class CategoryResultAdapter extends BaseAdapter {

    private final List<? extends BaseCategory> mCategories;
    private final LayoutInflater mInflater;


    public CategoryResultAdapter(Context context, List<? extends BaseCategory> categories) {
        mCategories = categories;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mCategories == null)
            return 0;
        return mCategories.size();
    }

    @Override
    public BaseCategory getItem(int i) {
        if (mCategories == null)
            return null;
        return mCategories.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (mCategories == null)
            return 0;
        return mCategories.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = mInflater.inflate(R.layout.simple_list_item_category, viewGroup, false);
        }
        BaseCategory c = getItem(i);
        if (c != null) {
            AQuery aq = new AQuery(v);
            aq.id(R.id.categoryTitle).text(c.getName());
            aq.id(R.id.categorySubcategories).text(c.getTotalSubs() + " categories");
            aq.id(R.id.categoryActiveItems).text(c.getActiveItems() + "");
            if (c.getTotalSubs() > 0) {
                aq.id(R.id.categorySubcategories).visible();
                aq.id(R.id.categoryArrow).visible();
                aq.id(R.id.categoryActiveItems).gone();
            } else {
                aq.id(R.id.categorySubcategories).gone();
                aq.id(R.id.categoryActiveItems).visible();
                aq.id(R.id.categoryArrow).gone();
            }

            // Deal exception
            if (c instanceof DealCategory) {
                aq.id(R.id.categoryActiveItems).gone();
            }
        }
        return v;
    }
}

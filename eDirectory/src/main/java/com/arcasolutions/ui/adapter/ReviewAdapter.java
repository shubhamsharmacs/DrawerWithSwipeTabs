package com.arcasolutions.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Review;

import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends BaseAdapter {

    protected final LayoutInflater mInflater;
    protected final Context mContext;
    private final List<Review> mItems;
    private final ImageOptions mImageOptions;

    public ReviewAdapter(Context context, List<Review> items) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mItems = items;
        mImageOptions = new ImageOptions();
        mImageOptions.fileCache = true;
        mImageOptions.memCache = true;
        mImageOptions.round = context.getResources().getDimensionPixelSize(R.dimen.reviewImageBoxSize);
    }

    @Override
    public int getCount() {
        if (mItems == null) return 0;
        return mItems.size();
    }

    @Override
    public Review getItem(int i) {
        if (mItems == null) return null;
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (getItem(i) == null) return 0;
        return getItem(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Review r = getItem(i);

        View v = convertView;
        if (v == null) {
            v = mInflater.inflate(R.layout.simple_list_item_review, viewGroup, false);
        }

        if (r != null) {
            AQuery aq = new AQuery(v);
            aq.id(R.id.reviewImage).image(r.getReviewerAvatarUrl(), mImageOptions);
            aq.id(R.id.reviewTitle).text(r.getTitle());
            aq.id(R.id.reviewDescription).text(r.getContent());
            aq.id(R.id.reviewAuthor).text(r.getReviewerName());
            aq.id(R.id.reviewDate).text(String.format(Locale.getDefault(), "%tD", r.getDateTime()));
            aq.id(R.id.reviewRating).rating(r.getRating());
        }

        return v;
    }

}
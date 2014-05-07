package com.arcasolutions.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.weedfinder.R;
import com.arcasolutions.api.model.Article;
import com.arcasolutions.api.model.Classified;
import com.arcasolutions.api.model.Deal;
import com.arcasolutions.api.model.Event;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.api.model.Module;

import java.util.LinkedList;
import java.util.Locale;

public class HomeResultAdapter<T extends Module> extends BaseAdapter {

    private static DisplayMetrics mMetrics;
    protected final LayoutInflater mInflater;
    protected final Context mContext;
    private final LinkedList<T> mItems;


    public HomeResultAdapter(Activity activity, LinkedList<T> items) {
        mContext = activity;
        mInflater = LayoutInflater.from(mContext);
        mItems = items;
        mMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    @Override
    public int getCount() {
        if (mItems == null) return 0;
        return mItems.size();
    }

    @Override
    public T getItem(int i) {
        if (mItems == null || i >= mItems.size()) return null;
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (mItems == null || i >= mItems.size()) return 0;
        return mItems.get(i).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Module item = getItem(i);

        if (item == null) return new View(mContext);

        if (item instanceof Article)
            return getView((Article) item, convertView, viewGroup);

        if (item instanceof Classified)
            return getView((Classified) item, convertView, viewGroup);

        if (item instanceof Deal)
            return getView((Deal) item, convertView, viewGroup);

        if (item instanceof Event)
            return getView(mInflater, (Event) item, convertView, viewGroup);

        if (item instanceof Listing)
            return getView((Listing) item, convertView, viewGroup);

        return new View(mContext);
    }

    //
    // Article information
    //
    public View getView(Article a, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        if (v == null) {
            v = mInflater.inflate(R.layout.simple_grid_item_article, viewGroup, false);
        }

        if (a != null) {
            AQuery aq = new AQuery(v);
            aq.id(R.id.articleImage).image(a.getImageUrl(), true, true);
            aq.id(R.id.articleTitle).text(a.getName());
            aq.id(R.id.articleAuthor).text(a.getAuthor());
        }

        handleGridItemSize(v, R.id.articleImage);
        return v;
    }

    //
    // Classified information
    //
    public View getView(Classified c, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        if (v == null) {
            v = mInflater.inflate(R.layout.simple_grid_item_classified, viewGroup, false);
        }

        if (c != null) {
            AQuery aq = new AQuery(v);
            aq.id(R.id.classifiedImage).image(c.getImageUrl(), true, true);
            aq.id(R.id.classifiedTitle).text(c.getName());
            aq.id(R.id.classifiedPrice).text(String.format("$%.2f", c.getPrice()));
        }

        handleGridItemSize(v, R.id.classifiedImage);
        return v;
    }

    //
    // Deal information
    //
    public View getView(Deal d, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        if (v == null) {
            v = mInflater.inflate(R.layout.simple_grid_item_deal, viewGroup, false);
        }


        if (d != null) {
            AQuery aq = new AQuery(v);
            aq.id(R.id.dealImage).image(d.getImageUrl(), true, true);
            aq.id(R.id.dealName).text(d.getTitle());
            aq.id(R.id.dealPrice).text(String.format("$%.2f", d.getDealValue()));
            aq.id(R.id.dealDiscount).text(String.format("%d%% OFF", (int) (Math.ceil((1 - (d.getDealValue() / d.getRealValue())) * 100))));
        }

        handleGridItemSize(v, R.id.dealImage);
        return v;
    }

    //
    // Event information
    //
    public static View getView(LayoutInflater inflater, Event e, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.simple_grid_item_event, viewGroup, false);
        }

        if (e != null) {
            AQuery aq = new AQuery(v);
            aq.id(R.id.eventTitle).text(e.getTitle());
            aq.id(R.id.eventImage).image(e.getImageUrl(), true, true);
            if (e.isRecurring()) {
                aq.id(R.id.label1).gone();
                aq.id(R.id.eventDate).text(e.getRecurringString());
            } else {
                aq.id(R.id.label1).visible();
                aq.id(R.id.eventDate).text(String.format(Locale.getDefault(), "%1$tD", e.getStartDate()));
            }
        }

        handleGridItemSize(v, R.id.eventImage);
        return v;
    }

    //
    // Listing information
    //
    public View getView(Listing l, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        if (v == null) {
            v = mInflater.inflate(R.layout.simple_grid_item_listing, viewGroup, false);
        }

        if (l != null) {
            AQuery aq = new AQuery(v);
            aq.id(R.id.listingImage).image(l.getImageUrl(), true, true);
            aq.id(R.id.listingTitle).text(l.getTitle());
            aq.id(R.id.listingRatingBar).rating(l.getRating());
            aq.id(R.id.listingDealBadge).getView()
                    .setVisibility(l.getDealId() != 0 ? View.VISIBLE : View.GONE);
        }

        handleGridItemSize(v, R.id.listingImage);

        return v;
    }

    private static void handleGridItemSize(View v, int imageId) {
        Context context = v.getContext();
        int height = context.getResources().getDimensionPixelSize(R.dimen.homeImageGridHeight);
        ImageView imageView = (ImageView) v.findViewById(imageId);
        int width = (int) Math.ceil(mMetrics.widthPixels / context.getResources().getInteger(R.integer.numHomeColumns));

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        imageView.setLayoutParams(layoutParams);
    }

}

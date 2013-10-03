package com.arcasolutions.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Article;
import com.arcasolutions.api.model.Deal;

import java.util.List;

public class ArticleResultAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Article> mArticles;

    public ArticleResultAdapter(Context context, List<Article> articles) {
        mArticles = articles;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mArticles == null) return 0;
        return mArticles.size();
    }

    @Override
    public Article getItem(int i) {
        if (mArticles == null) return null;
        return mArticles.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (mArticles == null) return 0;
        return mArticles.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = mInflater.inflate(R.layout.simple_list_item_article, viewGroup, false);
        }
        Article a = getItem(i);
        if (a != null) {
            AQuery aq = new AQuery(v);
            aq.id(R.id.articleImage).image(a.getImageUrl(), true, true);
            aq.id(R.id.articleTitle).text(a.getName());
            aq.id(R.id.articleAuthor).text(a.getAuthor());
        }

        return v;
    }
}

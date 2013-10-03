package com.arcasolutions.ui.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.SearchBy;
import com.arcasolutions.api.model.Article;
import com.arcasolutions.api.model.ArticleResult;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.Deal;
import com.arcasolutions.api.model.DealResult;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.ui.adapter.ArticleResultAdapter;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class ArticleResultActivity extends ActionBarActivity {

    public static final String EXTRA_ITEMS = "items";
    public static final String EXTRA_CATEGORY = "category";

    private BaseCategory mCategory;
    private int mPage;

    private final List<Article> mArticles = Lists.newArrayList();

    private ArticleResultAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_list_view);


        mAdapter = new ArticleResultAdapter(this, mArticles);

        AQuery aq = new AQuery(this);
        aq.id(android.R.id.list)
                .adapter(mAdapter);

        ArrayList<Parcelable> items = getIntent().getParcelableArrayListExtra(EXTRA_ITEMS);
        if (items != null) {
            for (Parcelable p : items) {
                if (p instanceof Listing) {
                    mArticles.add((Article) p);
                }
            }
            mAdapter.notifyDataSetChanged();
        }

        mCategory = getIntent().getParcelableExtra(EXTRA_CATEGORY);
        loadArticles();

    }

    private void loadArticles() {

        Client.Builder builder = new Client.Builder(ArticleResult.class);
        builder.page(mPage);
        if (mCategory != null) {
            builder.categoryId(mCategory.getId())
                .searchBy(SearchBy.CATEGORY);
        }
        builder.execAsync(new Client.RestListener<ArticleResult>() {

            @Override
            public void onComplete(ArticleResult result) {
                List<Article> articles = result.getResults();
                if (articles != null) {
                    mArticles.addAll(articles);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(Exception ex) {
                ex.printStackTrace();
            }
        });

    }


}

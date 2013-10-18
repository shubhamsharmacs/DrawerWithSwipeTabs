package com.arcasolutions.ui.fragment.article;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Article;
import com.google.common.base.Charsets;

import java.util.Locale;

public class ArticleOverviewFragment extends Fragment {

    public static final String ARG_ARTICLE = "article";

    public ArticleOverviewFragment() {
    }

    public static ArticleOverviewFragment newInstance(Article article) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_ARTICLE, article);
        final ArticleOverviewFragment f = new ArticleOverviewFragment();
        f.setArguments(args);
        return f;
    }

    public Article getShownArticle() {
        return getArguments() != null
                ? (Article) getArguments().getParcelable(ARG_ARTICLE)
                : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Article a = getShownArticle();
        if (a != null) {
            AQuery aq = new AQuery(view);
            aq.id(R.id.articleOverviewImage).image(a.getImageUrl(), true, true);
            aq.id(R.id.articleOverviewTitle).text(a.getName());
            aq.id(R.id.articleOverviewPublishDate).text(String.format(Locale.getDefault(), "Published at %tD by", a.getPubDate()));
            aq.id(R.id.articleOverviewAuthor).text(a.getAuthor());
            WebView webView = aq.id(R.id.articleOverviewContent).getWebView();
            webView.loadData(buildHtml(a.getContent()), "text/html", Charsets.UTF_8.displayName());
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setFocusable(false);
        }

    }

    private String buildHtml(String content) {

        StringBuilder builder = new StringBuilder();
        builder.append("<html><head>");
        builder.append("<style>body {color: black; margin:0px}</style>");
        builder.append("</head><body>");
        builder.append(content);
        builder.append("</body></html>");
        return builder.toString();
    }

}

package com.arcasolutions.ui.fragment.article;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.androidquery.AQuery;
import com.weedfinder.R;
import com.arcasolutions.api.model.Article;
import com.arcasolutions.ui.fragment.BaseFragment;
import com.arcasolutions.util.FavoriteHelper;
import com.arcasolutions.util.IntentUtil;
import com.google.common.base.Charsets;

import java.util.Locale;

public class ArticleOverviewFragment extends BaseFragment {

    public static final String ARG_ARTICLE = "article";
    private FavoriteHelper<Article> mFavoriteHelper;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoriteHelper = new FavoriteHelper<Article>(getActivity(), Article.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Article a = getShownArticle();
        if (a != null) {
            doShare(IntentUtil.share(a.getName(), a.getFriendlyUrl()));
            mFavoriteHelper.updateFavorite(a);
            AQuery aq = new AQuery(view);
            aq.id(R.id.articleOverviewImage).image(a.getImageUrl(), true, true);
            if (!URLUtil.isValidUrl(a.getImageUrl())) {
                aq.id(R.id.articleOverviewImage).gone();
                aq.id(R.id.articleOverviewFavorite).margin(0, 0, getResources().getDimension(R.dimen.spacingSmall), 0);
            }
            aq.id(R.id.articleOverviewTitle).text(a.getName());
            aq.id(R.id.articleOverviewPublishDate).text(String.format(Locale.getDefault(), getString(R.string.published_at_X_by), a.getPubDate()));
            aq.id(R.id.articleOverviewAuthor).text(a.getAuthor());
            final CheckBox favoriteCheckBox = aq.id(R.id.articleOverviewFavorite).getCheckBox();
            favoriteCheckBox.setChecked(mFavoriteHelper.isFavorited(a));
            favoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (!mFavoriteHelper.toggleFavorite(a)) {
                        favoriteCheckBox.setChecked(!b);
                    }
                }
            });
            WebView webView = aq.id(R.id.articleOverviewContent).getWebView();
            webView.loadData(buildHtml(a.getContent()), "text/html", Charsets.UTF_8.displayName());
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setFocusable(false);
        }

    }

    private String buildHtml(String content) {

        StringBuilder builder = new StringBuilder();
        builder.append("<html><head>");
        builder.append("<link href='http://fonts.googleapis.com/css?family=Roboto:300' rel='stylesheet' type='text/css'>");
        builder.append("<style>body {color: black; margin:0px; font-family: 'Roboto', sans-serif;}</style>");
        builder.append("</head><body>");
        builder.append(content);
        builder.append("</body></html>");
        return builder.toString();
    }

}

package com.arcasolutions.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.constant.ReviewModule;
import com.arcasolutions.api.model.Article;
import com.arcasolutions.api.model.ArticleResult;
import com.arcasolutions.api.model.Classified;
import com.arcasolutions.api.model.ClassifiedResult;
import com.arcasolutions.api.model.Deal;
import com.arcasolutions.api.model.DealResult;
import com.arcasolutions.api.model.Event;
import com.arcasolutions.api.model.EventResult;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.api.model.ListingResult;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.api.model.Review;
import com.arcasolutions.api.model.ReviewResult;
import com.arcasolutions.ui.activity.article.ArticleDetailActivity;
import com.arcasolutions.ui.activity.classified.ClassifiedDetailActivity;
import com.arcasolutions.ui.activity.event.EventDetailActivity;
import com.arcasolutions.ui.activity.listing.ListingDetailActivity;
import com.arcasolutions.ui.adapter.ReviewAdapter;
import com.arcasolutions.ui.fragment.HomeResultFragment;
import com.google.common.collect.Lists;
import com.origamilabs.library.views.StaggeredGridView;

import java.util.List;

public class HomeActivity extends BaseActivity implements HomeResultFragment.OnModuleSelectionListener {

    private HomeResultFragment mHomeResultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHomeResultFragment = (HomeResultFragment) getSupportFragmentManager().findFragmentByTag("home");
        if (mHomeResultFragment == null) {
            mHomeResultFragment = HomeResultFragment.newInstance(ArticleResult.class);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_content, mHomeResultFragment, "home")
                    .commit();
        }
    }

    @Override
    public void onModuleSelected(Module module, int position, long id) {
        if (module != null) {
            if (module instanceof Listing || module instanceof Deal) {
                Intent intent = new Intent(this, ListingDetailActivity.class);
                intent.putExtra(ListingDetailActivity.EXTRA_ID, module.getId());
                intent.putExtra(ListingDetailActivity.EXTRA_IS_DEAL, module instanceof Deal);
                startActivity(intent);
            } else if (module instanceof Article) {
                Intent intent = new Intent(this, ArticleDetailActivity.class);
                intent.putExtra(ArticleDetailActivity.EXTRA_ID, module.getId());
                startActivity(intent);
            } else if (module instanceof Classified) {
                Intent intent = new Intent(this, ClassifiedDetailActivity.class);
                intent.putExtra(ClassifiedDetailActivity.EXTRA_ID, module.getId());
                startActivity(intent);
            } else if (module instanceof Event) {
                Intent intent = new Intent(this, EventDetailActivity.class);
                intent.putExtra(EventDetailActivity.EXTRA_ID, module.getId());
                startActivity(intent);
            }
        }
    }


}

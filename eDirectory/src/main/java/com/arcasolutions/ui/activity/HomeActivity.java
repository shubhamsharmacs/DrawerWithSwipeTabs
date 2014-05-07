package com.arcasolutions.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.weedfinder.R;
import com.arcasolutions.api.model.Article;
import com.arcasolutions.api.model.BaseResult;
import com.arcasolutions.api.model.Classified;
import com.arcasolutions.api.model.Deal;
import com.arcasolutions.api.model.Event;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.OnModuleSelectionListener;
import com.arcasolutions.ui.activity.article.ArticleDetailActivity;
import com.arcasolutions.ui.activity.classified.ClassifiedDetailActivity;
import com.arcasolutions.ui.activity.event.EventDetailActivity;
import com.arcasolutions.ui.activity.listing.ListingDetailActivity;
import com.arcasolutions.ui.fragment.HomeResultFragment;
import com.arcasolutions.util.Util;

public class HomeActivity extends BaseActivity
        implements OnModuleSelectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HomeResultFragment mHomeResultFragment = (HomeResultFragment) getSupportFragmentManager().findFragmentByTag("home");
        if (mHomeResultFragment == null) {
            Class<? extends BaseResult> homeBaseResultClass = Util.getHomeClassResult(this);
            mHomeResultFragment = HomeResultFragment.newInstance(homeBaseResultClass);
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

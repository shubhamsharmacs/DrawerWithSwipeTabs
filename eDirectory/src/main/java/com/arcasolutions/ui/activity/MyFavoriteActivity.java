package com.arcasolutions.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.weedfinder.R;
import com.arcasolutions.api.model.Article;
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
import com.arcasolutions.ui.fragment.MyFavoriteFragment;

public class MyFavoriteActivity extends BaseActivity
        implements OnModuleSelectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyFavoriteFragment mFavoriteFragment = (MyFavoriteFragment) getSupportFragmentManager().findFragmentByTag("favorite");
        if (mFavoriteFragment == null) {
            mFavoriteFragment = new MyFavoriteFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_content, mFavoriteFragment, "favorite")
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
                if (module.getListingId() != null ) {
                    intent.putExtra(ListingDetailActivity.EXTRA_LISTING_ID, module.getListingId());
                }
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

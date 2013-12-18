package com.arcasolutions.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.arcasolutions.R;
import com.arcasolutions.api.model.ArticleCategory;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.BaseCategoryResult;
import com.arcasolutions.api.model.ClassifiedCategory;
import com.arcasolutions.api.model.DealCategory;
import com.arcasolutions.api.model.EventCategory;
import com.arcasolutions.api.model.ListingCategory;
import com.arcasolutions.ui.activity.article.ArticleResultActivity;
import com.arcasolutions.ui.activity.classified.ClassifiedResultActivity;
import com.arcasolutions.ui.activity.event.EventResultActivity;
import com.arcasolutions.ui.activity.listing.DealResultActivity;
import com.arcasolutions.ui.activity.listing.ListingResultActivity;
import com.arcasolutions.ui.fragment.CategoryResultFragment;

public class CategoryResultActivity extends BaseActivity
        implements CategoryResultFragment.OnCategorySelectionListener {

    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_CATEGORY = "category";

    private Class mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mType = (Class) getIntent().getSerializableExtra(EXTRA_TYPE);
        if (mType == null || BaseCategoryResult.class.isInstance(mType)) {
            finish();
            return;
        }

        BaseCategory mCategory = (BaseCategory) getIntent().getParcelableExtra(EXTRA_CATEGORY);

        CategoryResultFragment f = CategoryResultFragment.newInstance(mType, mCategory);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content, f)
                .commit();

    }

    @Override
    public void onCategorySelected(BaseCategory category) {
        if (category == null) return;

        if (category.getTotalSubs() > 0) {
            Intent intent = new Intent(this, getClass());
            intent.putExtra(EXTRA_TYPE, mType);
            intent.putExtra(EXTRA_CATEGORY, category);
            startActivity(intent);
        } else if (category.getActiveItems() > 0) {
            if (category instanceof ListingCategory) {
                Intent intent = new Intent(this, ListingResultActivity.class);
                intent.putExtra(ListingResultActivity.EXTRA_CATEGORY, category);
                startActivity(intent);
            } else if (category instanceof DealCategory) {
                Intent intent = new Intent(this, DealResultActivity.class);
                intent.putExtra(ListingResultActivity.EXTRA_CATEGORY, category);
                startActivity(intent);
            } else if (category instanceof ArticleCategory) {
                Intent intent = new Intent(this, ArticleResultActivity.class);
                intent.putExtra(ListingResultActivity.EXTRA_CATEGORY, category);
                startActivity(intent);
            } else if (category instanceof ClassifiedCategory) {
                Intent intent = new Intent(this, ClassifiedResultActivity.class);
                intent.putExtra(ListingResultActivity.EXTRA_CATEGORY, category);
                startActivity(intent);
            } else if (category instanceof EventCategory) {
                Intent intent = new Intent(this, EventResultActivity.class);
                intent.putExtra(ListingResultActivity.EXTRA_CATEGORY, category);
                startActivity(intent);
            }
        }
    }
}

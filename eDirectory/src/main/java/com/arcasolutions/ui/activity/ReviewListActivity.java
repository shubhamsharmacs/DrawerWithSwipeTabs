package com.arcasolutions.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.arcasolutions.R;
import com.arcasolutions.api.constant.ReviewModule;
import com.arcasolutions.ui.fragment.ReviewListFragment;

public class ReviewListActivity extends SecondLevelActivity {

    public static final String EXTRA_ITEM_ID = "itemId";
    public static final String EXTRA_MODULE = "module";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        Intent intent = getIntent();
        long itemId = intent.getLongExtra(EXTRA_ITEM_ID, 0);
        ReviewModule reviewModule = (ReviewModule) intent.getSerializableExtra(EXTRA_MODULE);

        if (itemId == 0 || reviewModule == null) {
            finish();
            return;
        }

        ReviewListFragment fragment = ReviewListFragment.newInstance(itemId, reviewModule);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content, fragment)
                .commit();

    }
}

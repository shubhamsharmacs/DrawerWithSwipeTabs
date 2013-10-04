package com.arcasolutions.ui.activity;

import android.os.Bundle;

import com.arcasolutions.R;
import com.arcasolutions.api.model.ArticleResult;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.ui.fragment.ModuleResultFragment;

public class ArticleResultActivity extends BaseActivity {

    public static final String EXTRA_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseCategory category = getIntent().getParcelableExtra(EXTRA_CATEGORY);

        ModuleResultFragment<ArticleResult> fragment = ModuleResultFragment.newInstance(ArticleResult.class, category);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content, fragment)
                .commit();
    }




}

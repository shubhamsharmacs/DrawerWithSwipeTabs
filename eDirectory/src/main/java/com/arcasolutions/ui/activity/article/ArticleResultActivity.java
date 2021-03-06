package com.arcasolutions.ui.activity.article;

import android.content.Intent;
import android.os.Bundle;

import com.weedfinder.R;
import com.arcasolutions.api.model.ArticleResult;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.OnModuleSelectionListener;
import com.arcasolutions.ui.activity.BaseActivity;
import com.arcasolutions.ui.fragment.ModuleResultFragment;

public class ArticleResultActivity extends BaseActivity
        implements OnModuleSelectionListener {

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


    @Override
    public void onModuleSelected(Module module, int position, long id) {
        if (module != null) {
            Intent intent = new Intent(this, ArticleDetailActivity.class);
            intent.putExtra(ArticleDetailActivity.EXTRA_ID, module.getId());
            startActivity(intent);
        }
    }
}

package com.arcasolutions.ui.activity.listing;

import android.content.Intent;
import android.os.Bundle;

import com.arcasolutions.R;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.ListingResult;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.activity.BaseActivity;
import com.arcasolutions.ui.fragment.ModuleResultFragment;

public class ListingResultActivity extends BaseActivity implements ModuleResultFragment.OnModuleSelectionListener {

    public static final String EXTRA_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseCategory category = getIntent().getParcelableExtra(EXTRA_CATEGORY);

        ModuleResultFragment<ListingResult> fragment = ModuleResultFragment.newInstance(ListingResult.class, category);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content, fragment)
                .commit();
    }


    @Override
    public void onModuleSelected(Module module, int position, long id) {
        if (module != null) {
            Intent intent = new Intent(this, ListingDetailActivity.class);
            intent.putExtra(ListingDetailActivity.EXTRA_ID, module.getId());
            startActivity(intent);
        }
    }
}

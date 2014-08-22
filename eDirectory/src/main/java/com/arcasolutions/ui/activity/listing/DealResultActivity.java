package com.arcasolutions.ui.activity.listing;

import android.content.Intent;
import android.os.Bundle;

import com.weedfinder.R;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.Deal;
import com.arcasolutions.api.model.DealResult;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.OnModuleSelectionListener;
import com.arcasolutions.ui.activity.BaseActivity;
import com.arcasolutions.ui.fragment.ModuleResultFragment;

public class DealResultActivity extends BaseActivity
        implements OnModuleSelectionListener {

    public static final String EXTRA_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseCategory category = getIntent().getParcelableExtra(EXTRA_CATEGORY);

        ModuleResultFragment<DealResult> fragment = ModuleResultFragment.newInstance(DealResult.class, category);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content, fragment)
                .commit();
    }


    @Override
    public void onModuleSelected(Module module, int position, long id) {
        if (module != null) {
            Intent intent = new Intent(this, ListingDetailActivity.class);
            intent.putExtra(ListingDetailActivity.EXTRA_ID, ((Deal)module).getListingId());
            intent.putExtra(ListingDetailActivity.EXTRA_IS_DEAL, true);
            if (module.getListingId() != null) {
                intent.putExtra(ListingDetailActivity.EXTRA_LISTING_ID, module.getListingId());
            }
            startActivity(intent);
        }
    }
}

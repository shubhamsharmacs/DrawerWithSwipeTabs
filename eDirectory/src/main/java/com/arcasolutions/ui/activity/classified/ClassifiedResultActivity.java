package com.arcasolutions.ui.activity.classified;

import android.content.Intent;
import android.os.Bundle;

import com.weedfinder.R;
import com.arcasolutions.api.model.BaseCategory;
import com.arcasolutions.api.model.ClassifiedResult;
import com.arcasolutions.api.model.Module;
import com.arcasolutions.ui.OnModuleSelectionListener;
import com.arcasolutions.ui.activity.BaseActivity;
import com.arcasolutions.ui.fragment.ModuleResultFragment;

public class ClassifiedResultActivity extends BaseActivity
        implements OnModuleSelectionListener {

    public static final String EXTRA_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseCategory category = getIntent().getParcelableExtra(EXTRA_CATEGORY);

        ModuleResultFragment<ClassifiedResult> fragment = ModuleResultFragment.newInstance(ClassifiedResult.class, category);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_content, fragment)
                .commit();

    }

    @Override
    public void onModuleSelected(Module module, int position, long id) {
        if (module != null) {
            Intent intent = new Intent(this, ClassifiedDetailActivity.class);
            intent.putExtra(ClassifiedDetailActivity.EXTRA_ID, module.getId());
            startActivity(intent);
        }
    }
}

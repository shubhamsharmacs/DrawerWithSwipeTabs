package com.arcasolutions.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arcasolutions.R;
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
import com.arcasolutions.ui.fragment.ModuleResultFragment;
import com.arcasolutions.ui.fragment.map.Filter;
import com.arcasolutions.ui.fragment.map.MyMapFilterFragment;
import com.arcasolutions.ui.fragment.map.MyMapFragment;

import java.util.ArrayList;

public class MapActivity extends BaseActivity
        implements MyMapFragment.OnShowAsListListener,
            FragmentManager.OnBackStackChangedListener,
        OnModuleSelectionListener {

    private MyMapFragment mMyMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        mMyMapFragment = (MyMapFragment) getSupportFragmentManager().findFragmentByTag("map");
        if (mMyMapFragment == null) {
            mMyMapFragment = new MyMapFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_content, mMyMapFragment, "map")
                    .commit();
        }
    }

    private void dismissFilter() {
        Fragment filterFragment = getSupportFragmentManager()
                .findFragmentByTag("filter");
        if (filterFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(filterFragment)
                    .commit();
        }

        supportInvalidateOptionsMenu();
    }

    private void showFilter() {
        Fragment filterFragment = getSupportFragmentManager().findFragmentByTag("filter");
        if (filterFragment == null) {
            filterFragment = MyMapFilterFragment.newInstance(mMyMapFragment.getFilter().getModuleIndex());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_content, filterFragment, "filter")
                    .commit();
        }

        supportInvalidateOptionsMenu();
    }

    public void onSearch(View view) {
        MyMapFilterFragment filterFragment = (MyMapFilterFragment) getSupportFragmentManager().findFragmentByTag("filter");
        if (filterFragment != null) {
            Filter filter = filterFragment.getFilter();
            dismissFilter();
            mMyMapFragment.setFilter(filter);
        }
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isFilterFragmentVisible = getSupportFragmentManager().findFragmentByTag("filter") != null;
        boolean isResultListFragmentVisible = getSupportFragmentManager().findFragmentByTag("list") != null;

        MenuItem menuFilter = menu.findItem(R.id.action_filter);
        menuFilter.setVisible(!isFilterFragmentVisible && !isResultListFragmentVisible);

        MenuItem menuCancel = menu.findItem(R.id.action_cancel);
        menuCancel.setVisible(isFilterFragmentVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                showFilter();
                return true;

            case R.id.action_cancel:
                dismissFilter();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onShowAsList(ArrayList<Module> result, Class<? extends BaseResult> clazz) {
        if (result == null || result.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_results_to_be_shown), Toast.LENGTH_SHORT).show();
            return;
        }

        ModuleResultFragment fragment = ModuleResultFragment.newInstance(clazz, result);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_content, fragment, "list")
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onBackStackChanged() {
        supportInvalidateOptionsMenu();
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

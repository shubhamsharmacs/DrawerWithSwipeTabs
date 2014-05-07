package com.arcasolutions.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weedfinder.R;
import com.arcasolutions.ui.fragment.BaseFragment;
import com.arcasolutions.util.IntentUtil;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AboutFragment mAboutFragment = (AboutFragment) getSupportFragmentManager().findFragmentByTag("about");
        if (mAboutFragment == null) {
            mAboutFragment = new AboutFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_content, mAboutFragment, "about")
                    .commit();
        }

    }

    public static class AboutFragment extends BaseFragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_about, container, false);
        }
    }

    public void onVisitWebsite(View view) {
        IntentUtil.website(this, getString(R.string.about_website_url));
    }

    public void onSendUsAnEmail(View view) {
        IntentUtil.email(this, getString(R.string.about_email_contact), getString(R.string.about_email_contact_subject), "");
    }

    public void onCallUs(View view) {
        IntentUtil.call(this, getString(R.string.about_telephone));
    }

}

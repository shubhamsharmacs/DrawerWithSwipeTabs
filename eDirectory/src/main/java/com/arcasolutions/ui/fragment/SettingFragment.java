package com.arcasolutions.ui.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.model.Account;
import com.arcasolutions.ui.activity.LoginActivity;
import com.arcasolutions.util.AccountHelper;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class SettingFragment extends Fragment implements AdapterView.OnItemClickListener {

    private SettingAdapter mAdapter;
    private AccountHelper mAccountHelper;

    private final IntentFilter mAccountChangedFilter = new IntentFilter(AccountHelper.ACTION_ACCOUNT_CHANGED);
    private final BroadcastReceiver mAccountChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountHelper = new AccountHelper(getActivity());
        mAdapter = new SettingAdapter(getActivity());
        getActivity().setTitle(R.string.drawerSetting);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        AQuery aq = new AQuery(view);
        aq.id(R.id.listView)
                .adapter(mAdapter)
                .itemClicked(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mAccountChangedReceiver, mAccountChangedFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mAccountChangedReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case SettingAdapter.ITEM_ABOUT_LEGAL_NOTICES:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.setting_legal_notices)
                        .setMessage(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(getActivity()))
                        .setPositiveButton(android.R.string.ok, null)
                        .create().show();
                break;

            case SettingAdapter.ITEM_ACCOUNT_VIEW:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;

        }
    }

    private static class SettingAdapter extends BaseAdapter {

        static final int ITEM_ACCOUNT_HEADER = 0;
        static final int ITEM_ACCOUNT_VIEW = 1;
        static final int ITEM_ABOUT_HEADER = 2;
        static final int ITEM_ABOUT_US = 3;
        static final int ITEM_ABOUT_LEGAL_NOTICES = 4;
        static final int ITEM_ABOUT_WATCH_APP_TUTORIAL = 5;
        static final int ITEM_ABOUT_VERSION = 6;

        private static final int VIEW_TYPE_HEADER = 0x100;
        private static final int VIEW_TYPE_NORMAL = 0x200;
        private static final int VIEW_TYPE_CUSTOM = 0x300;

        private final Context mContext;
        private final LayoutInflater mInflater;
        private final AccountHelper mAccountHelper;

        public SettingAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            mContext = context;
            mAccountHelper = new AccountHelper(mContext);
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            switch (getItemViewType(position))
            {
                case VIEW_TYPE_HEADER:
                    TextView textView;
                    if (view == null || VIEW_TYPE_HEADER != view.getTag()) {
                        textView = (TextView) mInflater.inflate(R.layout.simple_list_item_setting_header, parent, false);
                        textView.setTag(VIEW_TYPE_HEADER);
                    } else {
                        textView = (TextView) view;
                    }
                    textView.setText(position == ITEM_ACCOUNT_HEADER ? R.string.setting_account : R.string.setting_about);
                    view = textView;
                    break;

                case VIEW_TYPE_CUSTOM:
                    View viewItemAccount;
                    if (view == null || VIEW_TYPE_CUSTOM != view.getTag()) {
                        viewItemAccount = mInflater.inflate(R.layout.simple_list_item_setting_login, parent, false);
                        viewItemAccount.setTag(VIEW_TYPE_CUSTOM);
                    } else {
                        viewItemAccount = view;
                    }

                    AQuery aq2 = new AQuery(viewItemAccount);

                    final Account account = mAccountHelper.getAccount();
                    aq2.id(R.id.settingText1).visible()
                            .text(account != null
                                    ? "You are logged in as"
                                    : "You are not logged in");
                    aq2.id(R.id.settingText2).visible()
                            .text(account != null
                                    ? account.getFullName() + "\n" + account.getEmail()
                                    : "Login with you details or create a new account");

                    aq2.id(R.id.buttonLoginLogout).visible()
                            .text(account != null
                                    ? "Sign Out"
                                    : "Sign In")
                            .clicked(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (mAccountHelper.hasAccount()) {
                                        mAccountHelper.remove();
                                    } else {
                                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                                    }
                                }
                            });

                    view = viewItemAccount;
                    break;

                case VIEW_TYPE_NORMAL:
                    View viewItemNormal;
                    if (view == null || VIEW_TYPE_NORMAL != view.getTag()) {
                        viewItemNormal = mInflater.inflate(R.layout.simple_list_item_setting_normal, parent, false);
                        viewItemNormal.setTag(VIEW_TYPE_NORMAL);
                    } else {
                        viewItemNormal = view;
                    }
                    AQuery aq = new AQuery(viewItemNormal);
                    switch (position) {
                        case ITEM_ABOUT_US:
                            aq.id(R.id.settingText1).visible()
                                    .text(R.string.setting_about_us);
                            aq.id(R.id.settingText2).gone();
                            break;

                        case ITEM_ABOUT_LEGAL_NOTICES:
                            aq.id(R.id.settingText1).visible()
                                    .text(R.string.setting_legal_notices);
                            aq.id(R.id.settingText2).gone();
                            break;

                        case ITEM_ABOUT_WATCH_APP_TUTORIAL:
                            aq.id(R.id.settingText1).visible()
                                    .text(R.string.setting_what_app_tutorial);
                            aq.id(R.id.settingText2).gone();
                            break;

                        case ITEM_ABOUT_VERSION:
                            String versionName = "";
                            try {
                                String packageName = mContext.getPackageName();
                                PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
                                versionName = packageInfo.versionName;
                            } catch (Exception ignored) {}

                            aq.id(R.id.settingText1).visible()
                                    .text(R.string.app_name);
                            aq.id(R.id.settingText2).visible()
                                    .text(R.string.setting_appVersion,versionName);
                            break;
                    }
                    view = viewItemNormal;
                    break;
            }
            return view;
        }

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case 0:
                case 2:
                    return VIEW_TYPE_HEADER;

                case 1:
                    return VIEW_TYPE_CUSTOM;

                default:
                    return VIEW_TYPE_NORMAL;
            }
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemViewType(position) != VIEW_TYPE_HEADER;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }
    }

}

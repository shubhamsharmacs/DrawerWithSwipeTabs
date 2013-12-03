package com.arcasolutions.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.Account;
import com.arcasolutions.api.model.Deal;
import com.arcasolutions.api.model.IappResult;
import com.arcasolutions.ui.activity.LoginActivity;

public class RedeemHelper implements Client.RestIappListener<String>, PopupMenu.OnMenuItemClickListener {

    private Fragment mFragment;
    private Activity mActivity;
    private Deal mDeal;
    private View mRedeemPlaceView;
    private ProgressDialog mProgressDialog;

    private RedeemHelper(Activity activity) {
        mActivity = activity;
    }

    private RedeemHelper(Fragment fragment) {
        mFragment = fragment;
        mActivity = mFragment.getActivity();
    }

    public static RedeemHelper from(Activity activity) {
        return new RedeemHelper(activity);
    }

    public static RedeemHelper from(Fragment fragment) {
        return new RedeemHelper(fragment);
    }

    public void init(int viewGroupId, Deal deal) {
        if (deal == null || viewGroupId == 0) return;

        mDeal = deal;

        ViewGroup viewGroup = (ViewGroup) mActivity.findViewById(viewGroupId);
        if (viewGroup == null) return;

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        mRedeemPlaceView = inflater.inflate(R.layout.custom_redeem_view, null);
        viewGroup.addView(mRedeemPlaceView);

        AQuery aq = new AQuery(mRedeemPlaceView);
        aq.id(R.id.dealOverviewRedeemButton).clicked(this, "onRedeemClick");
        aq.id(R.id.dealOverviewShareRedeemButton).clicked(this, "showPopup");

        refreshViewData();
    }

    private void refreshViewData() {
        AQuery aq = new AQuery(mRedeemPlaceView);
        aq.id(R.id.dealOverviewRedeemCode).text(mDeal.getRedeemCode());

        if (TextUtils.isEmpty(mDeal.getRedeemCode())) {
            aq.id(R.id.redeemView).gone();
            aq.id(R.id.dealOverviewRedeemButton).visible();
        } else {
            aq.id(R.id.redeemView).visible();
            aq.id(R.id.dealOverviewRedeemButton).gone();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginActivity.AUTHENTICATION_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            redeemDeal();
        }
    }

    private void redeemDeal() {
        AccountHelper accountHelper = new AccountHelper(mActivity);
        if (accountHelper.hasAccount()) {
            Account account = accountHelper.getAccount();
            Client.IappBuilder
                    .newRedeemBuilder(account.getUsername(), mDeal.getId(), account.isFacebook())
                    .execAsync(this);
            mProgressDialog = DialogHelper.from(mActivity).progress("Redeeming Deal...");
        } else {
            Intent intent = new Intent(mActivity, LoginActivity.class);

            if (mFragment != null)
                mFragment.startActivityForResult(intent, LoginActivity.AUTHENTICATION_REQUEST_CODE);
            else
                mActivity.startActivityForResult(intent, LoginActivity.AUTHENTICATION_REQUEST_CODE);
        }
    }

    public void onRedeemClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setTitle("Redeem")
                .setMessage("Are you sure you want redeem this Deal?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        redeemDeal();
                    }
                });
        builder.create().show();

    }

    public void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(mActivity, v);
        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.redeem_share, popupMenu.getMenu());
        popupMenu.show();
    }

    @Override
    public void onSuccess(IappResult<String> iappResult) {
        mProgressDialog.dismiss();
        if (iappResult.isSuccess()) {
            mDeal.setRedeemCode(iappResult.getResult());
            refreshViewData();
        } else {
            onFail(new Exception(iappResult.getMessage()));
        }
    }

    @Override
    public void onFail(Exception ex) {
        mProgressDialog.dismiss();
        DialogHelper.from(mActivity).fail(ex.getMessage());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_send_by_email) {
            String subject = "My Redeem Code for " + mDeal.getTitle();
            String message = "<p>Deal: <strong>"+mDeal.getTitle()+"</strong><br />" +
                    "Listing: <strong>"+mDeal.getListingTitle()+"</strong><br />" +
                    "Redeem Code: <strong>"+mDeal.getRedeemCode()+"</strong><br />" +
                    "Link: <a href=\""+mDeal.getFriendlyUrl()+"\">"+mDeal.getFriendlyUrl()+"</a><br /></p>";
            IntentUtil.email(mActivity, null, subject, message);
            return true;
        }
        return false;
    }
}

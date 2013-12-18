package com.arcasolutions.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.Account;
import com.arcasolutions.api.model.IappResult;
import com.arcasolutions.api.model.Listing;
import com.arcasolutions.ui.activity.CheckInActivity;
import com.arcasolutions.ui.activity.LoginActivity;

public class CheckInHelper implements Client.RestIappListener, View.OnClickListener {

    private AccountHelper mAccountHelper;
    private Activity mActivity;
    private Fragment mFragment;
    private Listing mListing;
    private OnCheckInPostListener mListener;

    public interface OnCheckInPostListener {
        void onCheckInPosted();
    }

    public CheckInHelper(Activity activity, Listing listing) {
        mActivity = activity;
        mAccountHelper = new AccountHelper(mActivity);
        mListing = listing;
        if (activity instanceof OnCheckInPostListener) {
            mListener = (OnCheckInPostListener) activity;
        }
    }

    public CheckInHelper(Fragment fragment, Listing listing) {
        mActivity = fragment.getActivity();
        mFragment = fragment;
        mAccountHelper = new AccountHelper(mActivity);
        mListing = listing;
        if (fragment instanceof OnCheckInPostListener) {
            mListener = (OnCheckInPostListener) fragment;
        }
    }

    public void setCheckInButton(Button checkButton) {
        if (checkButton == null) return;

        checkButton.setOnClickListener(this);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginActivity.AUTHENTICATION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (mAccountHelper.hasAccount()) {
                    checkIn();
                }
            }
        }
    }

    private void requestLogin() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        if (mFragment != null) {
            mFragment.startActivityForResult(intent, LoginActivity.AUTHENTICATION_REQUEST_CODE);
        } else {
            mActivity.startActivityForResult(intent, LoginActivity.AUTHENTICATION_REQUEST_CODE);
        }
    }

    private void checkIn() {
        if (mListing.getTotalCheckins() > 0 && !(mActivity instanceof CheckInActivity)) {
            Intent intent = new Intent(mActivity, CheckInActivity.class);
            intent.putExtra(CheckInActivity.EXTRA_LISTING, mListing);
            mActivity.startActivity(intent);
            return;
        }

        if (!mAccountHelper.hasAccount()) {
            requestLogin();
            return;
        }

        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        final View view = mInflater.inflate(R.layout.dialog_check_in, null);
        final EditText commentView = (EditText) view.findViewById(R.id.commentView);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setTitle(R.string.check_in)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.post, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        postCheckIn(commentView.getText().toString());
                    }
                });
        final AlertDialog dialog = builder.create();


        commentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Button postButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                if (postButton != null) {
                    postButton.setEnabled(!TextUtils.isEmpty(charSequence));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        dialog.show();
        Button postButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (postButton != null)
            postButton.setEnabled(false);

    }

    @Override
    public void onClick(View view) {
        checkIn();
    }

    private ProgressDialog mProgress;

    private void postCheckIn(String message) {
        mProgress = DialogHelper.from(mActivity)
                .progress(R.string.posting);

        Account account = mAccountHelper.getAccount();
        Client.IappBuilder.newCheckInBuilder(
                mListing.getId(),
                account.getId(),
                account.getFullName(),
                message
        ).execAsync(CheckInHelper.this);
    }

    @Override
    public void onSuccess(IappResult iappResult) {
        mProgress.dismiss();
        mListing.setTotalCheckins(mListing.getTotalReviews() + 1);
        Toast.makeText(mActivity, R.string.checkin_posted, Toast.LENGTH_SHORT).show();
        if (mListener != null) {
            mListener.onCheckInPosted();
        }
    }

    @Override
    public void onFail(Exception ex) {
        mProgress.dismiss();
        DialogHelper.from(mActivity)
                .fail(ex.getMessage());
    }
}

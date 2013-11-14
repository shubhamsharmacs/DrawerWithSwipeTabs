package com.arcasolutions.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.Account;
import com.arcasolutions.api.model.IappResult;
import com.arcasolutions.api.model.ReviewResult;
import com.arcasolutions.ui.activity.LoginActivity;
import com.google.android.gms.internal.al;

public class ReviewHelper implements Client.RestIappListener<ReviewResult> {

    private Activity mActivity;
    private Fragment mFragment;
    private long mListingId;
    private AccountHelper mAccountHelper;
    private ProgressDialog mProgress;

    public ReviewHelper(Activity activity, long listingId) {
        mActivity = activity;
        mListingId = listingId;
        mAccountHelper = new AccountHelper(mActivity);
    }

    public ReviewHelper(Fragment fragment, long listingId) {
        this(fragment.getActivity(), listingId);
        mFragment = fragment;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LoginActivity.AUTHENTICATION_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {

            reviewIt();
        }
    }

    public void setAddReviewButton(Button button) {
        if (button == null) return;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewIt();
            }
        });
    }

    private void reviewIt() {

        if (!mAccountHelper.hasAccount()) {
            Intent intent = new Intent(mActivity, LoginActivity.class);
            if (mFragment != null) {
                mFragment.startActivityForResult(intent, LoginActivity.AUTHENTICATION_REQUEST_CODE);
            } else {
                mActivity.startActivityForResult(intent, LoginActivity.AUTHENTICATION_REQUEST_CODE);
            }
            return;
        }

        showReviewDialog();
    }

    private void showReviewDialog() {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        final View view = inflater.inflate(R.layout.dialog_review, null);

        final Account account = mAccountHelper.getAccount();
        final AQuery aq = new AQuery(view);
        long _accountId = 0;
        if (account != null) {
            aq.id(R.id.reviewFormName).text(account.getFullName());
            aq.id(R.id.reviewFormEmail).text(account.getEmail());
            _accountId = account.getId();
        }

        final long accountId = _accountId;

        LocationUtil.attach(aq.id(R.id.reviewFormLocation).getEditText(), 3 * 1000);

        final AlertDialog alert = new AlertDialog.Builder(mActivity)
                .setTitle(R.string.add_your_review)
                .setView(view)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, null)
                .create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button ok = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean valid = true;

                        String name = (String) aq.id(R.id.reviewFormName).getEditable().toString();
                        if (TextUtils.isEmpty(name)) {
                            aq.id(R.id.reviewFormName).getTextView().setError("Name is empty.");
                            valid = false;
                        }

                        String email = (String) aq.id(R.id.reviewFormEmail).getEditable().toString();
                        if (TextUtils.isEmpty(email)) {
                            aq.id(R.id.reviewFormEmail).getTextView().setError("Email is empty.");
                            valid = false;
                        }

                        String location = (String) aq.id(R.id.reviewFormLocation).getEditable().toString();
                        if (TextUtils.isEmpty(location)) {
                            aq.id(R.id.reviewFormLocation).getTextView().setError("Location is empty.");
                            valid = false;
                        }

                        String title = (String) aq.id(R.id.reviewFormTitle).getEditable().toString();
                        if (TextUtils.isEmpty(title)) {
                            aq.id(R.id.reviewFormTitle).getTextView().setError("Title is empty.");
                            valid = false;
                        }

                        String comment = (String) aq.id(R.id.reviewFormComment).getEditable().toString();
                        if (TextUtils.isEmpty(comment)) {
                            aq.id(R.id.reviewFormComment).getTextView().setError("Comment is empty.");
                            valid = false;
                        }

                        float rating = aq.id(R.id.reviewFormRating).getRatingBar().getRating();
                        if (rating == 0) {
                            RatingBar ratingBar = aq.id(R.id.reviewFormRating).getRatingBar();

                            Toast.makeText(mActivity, "Rating must at least 1", Toast.LENGTH_SHORT).show();
                            valid = false;
                        }

                        if (!valid) {
                            return;
                        }

                        postReview(accountId, name, email, title, comment, location, rating);
                        alert.dismiss();
                    }
                });
            }
        });

        alert.show();
    }

    private void postReview(long accountId, String name, String email, String title, String comment, String location, float rating) {

        Client.IappBuilder
                .newCreateReviewBuilder(
                        mListingId,
                        accountId,
                        title,
                        comment,
                        name,
                        email,
                        location,
                        rating
                ).execAsync(this);

        mProgress = DialogHelper.from(mActivity)
                .progress(R.string.posting);
    }

    @Override
    public void onSuccess(IappResult<ReviewResult> iappResult) {
        mProgress.dismiss();
        if (iappResult.isSuccess()) {
            Toast.makeText(mActivity, "Review posted!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            DialogHelper.from(mActivity)
                    .fail(iappResult.getMessage());
        }
    }

    @Override
    public void onFail(Exception ex) {
        mProgress.dismiss();
        DialogHelper.from(mActivity)
                .fail(ex.getMessage());
    }
}

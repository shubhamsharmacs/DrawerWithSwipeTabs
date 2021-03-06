package com.arcasolutions.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.weedfinder.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.Account;
import com.arcasolutions.api.model.IappResult;
import com.arcasolutions.util.AccountHelper;
import com.arcasolutions.util.DialogHelper;
import com.arcasolutions.util.Util;

public class SignUpActivity extends ActionBarActivity
        implements Client.RestIappListener<Account> {

    public static final int REQUEST_CODE_CREATE_ACCOUNT = 222;

    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mEmailView;
    private EditText mNickNameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private Button mSubmit;
    private ProgressDialog mProgress;

    private AccountHelper mAccountHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirstNameView = (EditText) findViewById(R.id.createAccountFirstName);
        mLastNameView = (EditText) findViewById(R.id.createAccountLastName);
        mEmailView = (EditText) findViewById(R.id.createAccountEmail);
        mNickNameView = (EditText) findViewById(R.id.createAccountNickName);
        mPasswordView = (EditText) findViewById(R.id.createAccountPassword);
        mConfirmPasswordView = (EditText) findViewById(R.id.createAccountConfirmPassword);
        mSubmit = (Button) findViewById(R.id.createAccountSubmit);

        mAccountHelper = new AccountHelper(this);
    }

    public void onSubmit(View view) {

        boolean valid = true;

        // first name
        String firstName = mFirstNameView.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.name_is_required));
            valid = false;
        } else {
            mFirstNameView.setError(null);
        }

        // last name
        String lastName = mLastNameView.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            mLastNameView.setError(getString(R.string.last_name_is_required));
            valid = false;
        } else {
            mLastNameView.setError(null);
        }

        // email
        String email = mEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.email_is_required));
            valid = false;
        } else if (!Util.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.enter_a_valid_email));
            valid = false;
        } else {
            mEmailView.setError(null);
        }

        // Nickname
        String nickname = mNickNameView.getText().toString();
        if (TextUtils.isEmpty(nickname)) {
            mNickNameView.setError(getString(R.string.nickname_is_required));
            valid = false;
        } else {
            mNickNameView.setError(null);
        }

        // password
        String password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.password_is_required));
            valid = false;
        } else if (password.length() < 4) {
            mPasswordView.setError(getString(R.string.minimum_of_4_chars));
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        // confirm password
        String cofirmPassword = mConfirmPasswordView.getText().toString();
        if (!password.equals(cofirmPassword)) {
            mConfirmPasswordView.setError(getString(R.string.confirm_password_does_not_match));
            valid = false;
        } else {
            mConfirmPasswordView.setError(null);
        }

        if (valid) {
            setEnable(false);
            Client.IappBuilder.newCreateAcccountBuilder(
                    email, password, firstName, lastName, nickname)
                        .execAsync(this);
            mProgress = DialogHelper.from(this).progress(getString(R.string.creating_account));
        }

    }

    private void setEnable(boolean enable) {
        mFirstNameView.setEnabled(enable);
        mLastNameView.setEnabled(enable);
        mNickNameView.setEnabled(enable);
        mEmailView.setEnabled(enable);
        mPasswordView.setEnabled(enable);
        mConfirmPasswordView.setEnabled(enable);
        mSubmit.setEnabled(enable);
    }

    @Override
    public void onSuccess(IappResult<Account> iappResult) {
        mProgress.dismiss();
        setEnable(true);

        try {
            if (!iappResult.isSuccess()) {
                throw new Exception(iappResult.getMessage());
            }

            Account account = iappResult.getResult();
            mAccountHelper.store(account);

            setResult(Activity.RESULT_OK);
            finishActivity(REQUEST_CODE_CREATE_ACCOUNT);
            finish();

        } catch (Exception e) {
            DialogHelper.from(this).fail(iappResult.getMessage());
        }

    }

    @Override
    public void onFail(Exception ex) {
        setEnable(true);
        mProgress.dismiss();
        DialogHelper.from(this).fail(ex.getMessage());
    }
}

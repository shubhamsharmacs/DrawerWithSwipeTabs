package com.arcasolutions.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.arcasolutions.App;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.Account;
import com.arcasolutions.api.model.IappResult;
import com.arcasolutions.util.AccountHelper;
import com.arcasolutions.util.DialogHelper;
import com.arcasolutions.web.FacebookWebOAuthActivity;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;

import java.sql.SQLException;

public class LoginActivity extends ActionBarActivity
            implements Client.RestIappListener<Account> {

    private EditText emailView;
    private EditText passwordView;
    private AccountHelper mAccountHelper;
    private ProgressDialog mProgress;
    private App mApp;

    public static final int AUTHENTICATION_REQUEST_CODE = 0x1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mApp = (App) getApplicationContext();

        mAccountHelper = new AccountHelper(this);

        AQuery aq = new AQuery(this);
        emailView = aq.id(R.id.loginEmail).getEditText();
        passwordView = aq.id(R.id.loginPassword).getEditText();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FacebookWebOAuthActivity.REQUEST_FACEBOOK_PERMISSION) {
                loginWithFacebook();
            }
        }
    }

    public void onLogin(View view) {

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        boolean valid = true;
        if (TextUtils.isEmpty(email)) {
            emailView.setError("Email should be filled.");
            valid = false;
        } else {
            emailView.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordView.setError("Password should be filled.");
            valid = false;
        } else {
            passwordView.setError(null);
        }

        if (valid) {
            mProgress = DialogHelper.from(this).progress(R.string.alert_progress_authenticating);
            Client.IappBuilder.newAuthenticateAccountBuilder(email, password)
                    .execAsync(this);
        }
    }

    public void onLoginWithFacebook(View view) {
        if (mApp.isFacebookConnected()) {
            mApp.facebookDisconnect();
        }

        Intent intent = new Intent(this, FacebookWebOAuthActivity.class);
        startActivityForResult(intent, FacebookWebOAuthActivity.REQUEST_FACEBOOK_PERMISSION);
    }

    private void loginWithFacebook() {
        if (!mApp.isFacebookConnected()) {
            DialogHelper.from(this).fail("You are not connected on facebook.");
            return;
        }

        Facebook facebook = mApp.getFacebook();
        mProgress = DialogHelper.from(this).progress(R.string.alert_progress_authenticating);
        FacebookProfile profile = facebook.userOperations().getUserProfile();
        Client.IappBuilder.newAuthenticateFacebookBuilder(
                profile.getId(),
                profile.getEmail(),
                profile.getFirstName(),
                profile.getLastName()
        );
    }

    @Override
    public void onSuccess(IappResult<Account> result) {
        mProgress.dismiss();
        if (result.isSuccess()) {
            try {
                mAccountHelper.store(result.getResult());
                setResult(RESULT_OK);
                finishActivity(AUTHENTICATION_REQUEST_CODE);
                finish();
            } catch (SQLException e) {
                DialogHelper.from(this).fail(e.getMessage());
            }
        } else {
            DialogHelper.from(this).fail(result.getMessage());
        }
    }

    @Override
    public void onFail(Exception ex) {
        mProgress.dismiss();
        DialogHelper.from(this).fail(ex.getMessage());
    }
}

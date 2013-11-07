package com.arcasolutions.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.arcasolutions.R;
import com.arcasolutions.api.Client;
import com.arcasolutions.api.model.Account;
import com.arcasolutions.api.model.IappResult;
import com.arcasolutions.util.AccountHelper;
import com.arcasolutions.util.DialogHelper;

import java.sql.SQLException;

public class LoginActivity extends ActionBarActivity
            implements Client.RestIappListener<Account> {

    private EditText emailView;
    private EditText passwordView;
    private AccountHelper mAccountHelper;
    private ProgressDialog mProgress;

    public static final int AUTHENTICATION_REQUEST_CODE = 0x1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAccountHelper = new AccountHelper(this);

        AQuery aq = new AQuery(this);
        emailView = aq.id(R.id.loginEmail).getEditText();
        passwordView = aq.id(R.id.loginPassword).getEditText();
    }

    public void onLogin(View view) {
        Toast.makeText(this, "onLogin", Toast.LENGTH_SHORT).show();


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
        Toast.makeText(this, "onLoginWithFacebook", Toast.LENGTH_SHORT).show();

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

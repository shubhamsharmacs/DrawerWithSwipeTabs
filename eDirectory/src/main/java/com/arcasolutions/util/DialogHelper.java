package com.arcasolutions.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.arcasolutions.R;

public class DialogHelper {

    private Context mContext;

    private DialogHelper(Context context) {
        mContext = context;
    }

    public static DialogHelper from(Context context) {
        return new DialogHelper(context);
    }

    public void fail(CharSequence message) {
        alert(mContext.getString(R.string.alert_title_fail),
            mContext.getString(R.string.alert_message_fail, message));
    }

    public void alert(CharSequence title, CharSequence message) {
        new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create().show();
    }

    public ProgressDialog progress(int messageId) {
        return progress(0, messageId);
    }

    public ProgressDialog progress(int title, int message) {
        return ProgressDialog.show(
                mContext,
                title == 0 ? null : mContext.getString(title),
                mContext.getString(message));
    }

}

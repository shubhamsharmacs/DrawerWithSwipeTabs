package com.arcasolutions.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;

public final class IntentUtil {

    public static void call(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    public static void website(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void email(Activity activity, String to, String subject, String message) {
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(activity);
        builder.setType("message/rfc822")
            .addEmailTo(to)
            .setSubject(subject)
            .setHtmlText(message)
            .startChooser();
    }

    public static Intent share(String message, String friendlyUrl) {
        if (TextUtils.isEmpty(message)) message = "";
        if (!TextUtils.isEmpty(friendlyUrl)) message += " " + friendlyUrl;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, message);
        shareIntent.putExtra(Intent.EXTRA_TEXT, message + " " + friendlyUrl);
        shareIntent.putExtra("message", message);
        return shareIntent;
    }

}

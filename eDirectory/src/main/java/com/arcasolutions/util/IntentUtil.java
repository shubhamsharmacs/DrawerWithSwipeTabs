package com.arcasolutions.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import com.arcasolutions.ui.activity.MenuWebviewActivity;
import com.weedfinder.R;

import java.util.Locale;

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
            .addEmailTo(Util.isEmailValid(to) ? to : "")
            .setSubject(subject != null ? subject : "")
            .setHtmlText(message != null ? message : "")
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

    public static void openWebView(Context context, String url) {
        Intent intent = new Intent(context, MenuWebviewActivity.class);
        intent.putExtra(MenuWebviewActivity.EXTRA_URL, url);
        context.startActivity(intent);

    }

    public static void  getDirections(Context context, double lat, double lng) {
        Location myLocation = Util.getMyLocation();
        if (myLocation == null) return;

        try {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(String.format(Locale.US, "http://maps.google.com/maps?saddr=%1$.9f,%2$.9f&daddr=%3$.9f,%4$.9f",
                            myLocation.getLatitude(),
                            myLocation.getLongitude(),
                            lat,
                            lng)));
            context.startActivity(intent);
        }  catch (Exception e) {
            Toast.makeText(context, context.getText(R.string.no_app_to_handle_this_operation), Toast.LENGTH_SHORT).show();
        }
    }


}

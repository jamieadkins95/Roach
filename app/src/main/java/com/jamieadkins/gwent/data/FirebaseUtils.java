package com.jamieadkins.gwent.data;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.R;

import static com.jamieadkins.gwent.about.SettingsActivity.PREFERENCE_ANALYTICS;

/**
 * Misc utils for firebase.
 */

public class FirebaseUtils {

    private static boolean persistanceSet = false;

    public static FirebaseDatabase getDatabase() {
        if (!persistanceSet) {
            // Enable offline use. This has to be done before any other firebase database work.
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            persistanceSet = true;
        }

        return FirebaseDatabase.getInstance();
    }

    public static void askForAnalyticsPermission(final Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (!preferences.contains(PREFERENCE_ANALYTICS)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            // Analytics are off by default.
            onAnalyticsResponse(context, false);

            builder.setTitle(R.string.analytics)
                    .setMessage(R.string.analytics_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            onAnalyticsResponse(context, true);
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .setNeutralButton(R.string.contact, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse("mailto:"));
                            intent.putExtra(Intent.EXTRA_EMAIL,
                                    context.getResources().getStringArray(R.array.developer_email));
                            intent.putExtra(Intent.EXTRA_SUBJECT,
                                    context.getString(R.string.email_subject));

                            String template = String.format(
                                    context.getString(R.string.email_template),
                                    context.getString(R.string.analytics));
                            intent.putExtra(Intent.EXTRA_TEXT, template);

                            if (intent.resolveActivity(context.getPackageManager()) != null) {
                                context.startActivity(intent);
                            }
                        }
                    })
                    .create()
                    .show();
        }
    }

    private static void onAnalyticsResponse(Context context, boolean response) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit();
        editor.putBoolean(PREFERENCE_ANALYTICS, response);
        editor.apply();

        FirebaseAnalytics.getInstance(context).setAnalyticsCollectionEnabled(response);
    }

    public static void logCard(Context context, String cardId, String cardName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, cardId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, cardName);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "card");
        FirebaseAnalytics.getInstance(context)
                .logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}

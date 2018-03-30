package com.jamieadkins.gwent.data;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.jamieadkins.gwent.R;

import static com.jamieadkins.gwent.settings.SettingsActivity.PREFERENCE_ANALYTICS;

/**
 * Misc utils for firebase.
 */

public class FirebaseUtils {
    public static final String STORAGE_BUCKET = "gs://gwent-9e62a.appspot.com/";

    private static boolean persistanceSet = false;

    public static FirebaseDatabase getDatabase() {
        if (!persistanceSet) {
            // Enable offline use. This has to be done before any other firebase database work.
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            persistanceSet = true;
        }

        return FirebaseDatabase.getInstance();
    }
}

package com.jamieadkins.gwent.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.BuildConfig;
import android.support.v7.preference.PreferenceManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingsActivity extends BasePreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String PREFERENCE_ANALYTICS = "com.jamieadkins.gwent.analytics";
    public static final String NOTIFICATIONS_NEWS = "com.jamieadkins.gwent.notifications.news";
    public static final String NOTIFICATIONS_PATCH = "com.jamieadkins.gwent.notifications.patch";

    SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case PREFERENCE_ANALYTICS:
                boolean enableAnalytics = sharedPreferences.getBoolean(PREFERENCE_ANALYTICS, false);
                FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(enableAnalytics);
                break;
            default:
                onSettingsChange(sharedPreferences, key);
                break;
        }
    }

    public static void onSettingsChange(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case NOTIFICATIONS_NEWS:
                boolean news = sharedPreferences.getBoolean(NOTIFICATIONS_NEWS, false);
                if (news) {
                    FirebaseMessaging.getInstance().subscribeToTopic("news");
                    if (BuildConfig.DEBUG) {
                        FirebaseMessaging.getInstance().subscribeToTopic("news-debug");
                    }
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                    if (BuildConfig.DEBUG) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("news-debug");
                    }
                }
                break;
            case NOTIFICATIONS_PATCH:
                boolean patch = sharedPreferences.getBoolean(NOTIFICATIONS_PATCH, true);
                if (patch) {
                    FirebaseMessaging.getInstance().subscribeToTopic("patch");
                    if (BuildConfig.DEBUG) {
                        FirebaseMessaging.getInstance().subscribeToTopic("patch-debug");
                    }
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("patch");
                    if (BuildConfig.DEBUG) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("patch-debug");
                    }
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}

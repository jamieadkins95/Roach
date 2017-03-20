package com.jamieadkins.gwent.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
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
            case NOTIFICATIONS_NEWS:
                boolean news = sharedPreferences.getBoolean(NOTIFICATIONS_NEWS, false);
                if (news) {
                    FirebaseMessaging.getInstance().subscribeToTopic("news");
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
                }
                break;
            case NOTIFICATIONS_PATCH:
                boolean patch = sharedPreferences.getBoolean(NOTIFICATIONS_PATCH, false);
                if (patch) {
                    FirebaseMessaging.getInstance().subscribeToTopic("patch");
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("patch");
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

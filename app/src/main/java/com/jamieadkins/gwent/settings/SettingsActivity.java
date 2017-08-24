package com.jamieadkins.gwent.settings;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.R;

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
                onSettingsChange(sharedPreferences, getResources(), key);
                break;
        }
    }

    public static void onSettingsChange(SharedPreferences sharedPreferences, Resources resources, String key) {
        switch (key) {
            case NOTIFICATIONS_NEWS:
                unsubscribeFromAllNews(resources);
                String news = sharedPreferences.getString(NOTIFICATIONS_NEWS, "none");

                if (news.equals("none")) {
                    break;
                }

                String topic = "news-" + news;
                FirebaseMessaging.getInstance().subscribeToTopic(topic);
                if (BuildConfig.DEBUG) {
                    FirebaseMessaging.getInstance().subscribeToTopic(topic + "-debug");
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

    private static void unsubscribeFromAllNews(Resources resources) {
        for (String key : resources.getStringArray(R.array.locales_news)) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("news-" + key);
            if (BuildConfig.DEBUG) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("news-" + key + "-debug");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}

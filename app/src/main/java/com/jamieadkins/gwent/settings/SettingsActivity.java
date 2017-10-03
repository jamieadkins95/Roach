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
    public static final String LOCALE = "com.jamieadkins.gwent.crowd.locale";

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
            case LOCALE:
                setResult(RESULT_OK);
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
                checkAndUpdatePatchTopic(sharedPreferences, resources);
                break;
        }
    }

    public static void checkAndUpdatePatchTopic(SharedPreferences sharedPreferences, Resources resources) {
        boolean subscribed = sharedPreferences.getBoolean(NOTIFICATIONS_PATCH, true);
        String intendedTopic = "patch-" + BuildConfig.CARD_DATA_VERSION;
        String key = resources.getString(R.string.pref_patch_notifications_topic_key);
        String topic = sharedPreferences.getString(key, null);
        if (subscribed) {
            if (!intendedTopic.equals(topic)) {
                if (topic != null) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                }
                FirebaseMessaging.getInstance().subscribeToTopic(intendedTopic);
                sharedPreferences.edit().putString(key, intendedTopic).apply();
            }
        } else {
            if (topic != null) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                sharedPreferences.edit().remove(key).apply();
            }
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

package com.jamieadkins.gwent.about;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;

import com.google.firebase.analytics.FirebaseAnalytics;

public class SettingsActivity extends BasePreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String PREFERENCE_ANALYTICS = "com.jamieadkins.gwent.analytics";

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
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}

package com.jamieadkins.gwent.settings;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

/**
 * Shows information about the app.
 */

public class PreferenceFragment extends PreferenceFragmentCompat {
    private static final String STATE_LAYOUT = "com.jamieadkins.gwent.layout";
    private int layout;

    public static PreferenceFragment newInstance(int layout) {
        PreferenceFragment fragment = new PreferenceFragment();
        fragment.layout = layout;
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource.
        if (savedInstanceState == null) {
            addPreferencesFromResource(layout);
        } else {
            layout = savedInstanceState.getInt(STATE_LAYOUT);
            addPreferencesFromResource(layout);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_LAYOUT, layout);
        super.onSaveInstanceState(outState);
    }
}

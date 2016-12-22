package com.jamieadkins.gwent.about;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.jamieadkins.gwent.R;

/**
 * Shows information about the app.
 */

public class AboutFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}

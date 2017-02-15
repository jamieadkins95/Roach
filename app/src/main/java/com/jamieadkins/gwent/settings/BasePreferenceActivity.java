package com.jamieadkins.gwent.settings;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseActivity;

/**
 * Shows card image and details.
 */

public class BasePreferenceActivity extends BaseActivity {
    public static final String EXTRA_PREFERENCE_LAYOUT = "com.jamieadkins.gwent.preference";
    public static final String EXTRA_PREFERENCE_TITLE = "com.jamieadkins.gwent.preference.title";

    @Override
    public void initialiseContentView() {
        setContentView(R.layout.activity_preference);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getIntent().getIntExtra(EXTRA_PREFERENCE_TITLE, -1));

        final int layout = getIntent().getIntExtra(EXTRA_PREFERENCE_LAYOUT, -1);
        PreferenceFragment fragment = PreferenceFragment.newInstance(layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(
                R.id.contentContainer, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Same behaviour as back button.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

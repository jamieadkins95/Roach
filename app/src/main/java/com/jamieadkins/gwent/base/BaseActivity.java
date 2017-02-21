package com.jamieadkins.gwent.base;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jamieadkins.gwent.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseContentView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set default values of our settings once, on first launch.
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }

    public abstract void initialiseContentView();

    public void showSnackbar(String message) {
        showSnackbar(message, null, null);
    }

    public void showSnackbar(String message, String actionString, View.OnClickListener action) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.coordinator_layout_for_snackbar),
                message,
                Snackbar.LENGTH_LONG);

        if (action != null) {
            snackbar.setAction(actionString, action);
        }

        snackbar.show();
    }
}

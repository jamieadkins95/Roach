package com.jamieadkins.gwent.base;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jamieadkins.gwent.R;
import static com.jamieadkins.gwent.settings.SettingsActivity.onSettingsChange;

public abstract class BaseActivity extends AppCompatActivity implements SnackbarShower,
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseContentView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        // Set default values of our settings once, on first launch.
        PreferenceManager.setDefaultValues(this, R.xml.settings, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.notification_channel_id);
            String channelName = getString(R.string.notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        onSettingsChange(sharedPreferences, key);
    }

    public abstract void initialiseContentView();

    @Override
    public void showSnackbar(String message) {
        showSnackbar(message, null, null);
    }

    @Override
    public void showSnackbar(String message, String actionString, View.OnClickListener action) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.coordinator_layout),
                message,
                Snackbar.LENGTH_LONG);

        if (action != null) {
            snackbar.setAction(actionString, action);
        }

        snackbar.show();
    }
}

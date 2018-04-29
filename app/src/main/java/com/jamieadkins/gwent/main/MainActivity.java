package com.jamieadkins.gwent.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.MenuItem;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseActivity;
import com.jamieadkins.commonutils.bus.RxBus;
import com.jamieadkins.gwent.bus.ScrollToTopEvent;
import com.jamieadkins.gwent.card.list.CardDatabaseFragment;
import com.jamieadkins.gwent.settings.PreferenceFragment;
import com.jamieadkins.gwent.settings.SettingsActivity;

import java.util.Locale;

public class MainActivity extends BaseActivity {
    private static final String STATE_NEWS_SHOWN = "com.jamieadkins.gwent.news.shown";

    private boolean newsItemShown = false;

    private BottomNavigationView navigationView;

    @Override
    public void initialiseContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            newsItemShown = savedInstanceState.getBoolean(STATE_NEWS_SHOWN, false);
        }

        checkLanguage();
        checkIntentForNews();
        SettingsActivity.checkAndUpdatePatchTopic(PreferenceManager.getDefaultSharedPreferences(this), getResources());

        if (savedInstanceState == null) {
            // Cold start, launch card db fragment.
            Fragment fragment = new CardDatabaseFragment();
            launchFragment(fragment, fragment.getClass().getSimpleName());
        }

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = getFragmentForMenuItem(item.getItemId());
                launchFragment(fragment, fragment.getClass().getSimpleName());
                return true;
            }
        });

        navigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                RxBus.INSTANCE.post(new ScrollToTopEvent());
            }
        });
    }

    private void checkIntentForNews() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String url = intent.getExtras().getString("url");
            if (url != null && !newsItemShown) {
                showChromeCustomTab(url);
                newsItemShown = true;
            }
        }
    }

    private void checkLanguage() {
        String language = Locale.getDefault().getLanguage();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.contains(getString(R.string.shown_language))) {
            SharedPreferences.Editor editor = preferences.edit();
            String[] locales = getResources().getStringArray(R.array.locales);
            for (String locale : locales) {
                if (locale.contains(language)) {
                    editor.putString(getString(R.string.pref_locale_key), locale);
                }
            }
            editor.putBoolean(getString(R.string.shown_language), true);
            editor.apply();
        }

        if (!preferences.contains(getString(R.string.shown_news))) {
            SharedPreferences.Editor editor = preferences.edit();
            String[] locales = getResources().getStringArray(R.array.locales_news);
            for (String locale : locales) {
                if (locale.contains(language)) {
                    editor.putString(getString(R.string.pref_news_notifications_key), locale);
                }
            }
            editor.putBoolean(getString(R.string.shown_news), true);
            editor.apply();
        }
    }

    private Fragment getFragmentForMenuItem(int itemId) {
        switch (itemId) {
            case R.id.navigation_card_db:
                return new CardDatabaseFragment();
            case R.id.navigation_collection:
                return new CardDatabaseFragment();
            case R.id.navigation_decks:
                return new CardDatabaseFragment();
            case R.id.navigation_gwent:
                return PreferenceFragment.newInstance(R.xml.gwent);
            default:
                return new CardDatabaseFragment();
        }
    }

    private void launchFragment(final Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(
                R.id.contentContainer, fragment, tag);
        fragmentTransaction.commit();

        // Our options menu will be different for different tabs.
        invalidateOptionsMenu();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_NEWS_SHOWN, newsItemShown);
        super.onSaveInstanceState(outState);
    }

    private void showChromeCustomTab(String url) {
        new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.gwentGreen))
                .setShowTitle(true)
                .build()
                .launchUrl(this, Uri.parse(url));
    }
}

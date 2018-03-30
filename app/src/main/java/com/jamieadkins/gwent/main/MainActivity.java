package com.jamieadkins.gwent.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.ComingSoonFragment;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseActivity;
import com.jamieadkins.gwent.bus.RxBus;
import com.jamieadkins.gwent.card.list.CardDatabaseFragment;
import com.jamieadkins.gwent.deck.list.NewDeckDialog;
import com.jamieadkins.gwent.settings.BasePreferenceActivity;
import com.jamieadkins.gwent.settings.SettingsActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends BaseActivity implements
        Drawer.OnDrawerItemClickListener {
    private static final String TAG_CARD_DB = "com.jamieadkins.gwent.CardDb";
    private static final String TAG_PUBLIC_DECKS = "com.jamieadkins.gwent.PublicDecks";
    private static final String TAG_USER_DECKS = "com.jamieadkins.gwent.UserDecks";
    private static final String TAG_COLLECTION = "com.jamieadkins.gwent.Collection";
    private static final String TAG_RESULTS_TRACKER = "com.jamieadkins.gwent.ResultsTracker";

    private static final String STATE_NEWS_SHOWN = "com.jamieadkins.gwent.news.shown";
    private static final int NO_LAUNCH_ATTEMPT = -1;

    private int mCurrentTab;
    private boolean newsItemShown = false;

    private Drawer mNavigationDrawer;
    private AccountHeader mAccountHeader;
    private Map<Integer, PrimaryDrawerItem> mDrawerItems;

    private FloatingActionButton buttonNewDeck;

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

        buttonNewDeck = findViewById(R.id.new_deck);
        buttonNewDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewDeckDialog newDeckDialog = new NewDeckDialog();
                newDeckDialog.show(getSupportFragmentManager(), newDeckDialog.getClass().getSimpleName());
            }
        });

        checkLanguage();
        checkIntentForNews();
        SettingsActivity.checkAndUpdatePatchTopic(PreferenceManager.getDefaultSharedPreferences(this), getResources());
        mAccountHeader = new AccountHeaderBuilder()
                .withHeaderBackground(R.drawable.header)
                .withProfileImagesVisible(false)
                .withActivity(this)
                .build();

        initialiseDrawerItems();
        mNavigationDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar((Toolbar) findViewById(R.id.toolbar))
                .withShowDrawerOnFirstLaunch(true)
                .withAccountHeader(mAccountHeader)
                .addDrawerItems(
                        mDrawerItems.get(R.id.tab_card_db),
                        mDrawerItems.get(R.id.tab_news),
                        mDrawerItems.get(R.id.tab_helper),
                        new SectionDrawerItem()
                                .withName(R.string.my_stuff)
                                .withDivider(false),
                        mDrawerItems.get(R.id.tab_decks),
                        mDrawerItems.get(R.id.tab_collection)
                )
                .addStickyDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(R.id.action_settings).
                                withName(R.string.settings)
                                .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_settings, getTheme()))
                                .withSelectable(false),
                        new PrimaryDrawerItem()
                                .withIdentifier(R.id.action_about).
                                withName(R.string.about)
                                .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_info, getTheme()))
                                .withSelectable(false)
                )
                .withOnDrawerItemClickListener(this)
                .build();

        if (savedInstanceState == null) {
            // Cold start, launch card db fragment.
            mNavigationDrawer.setSelection(R.id.tab_card_db);
        } else {
            // Need to find out which fragment we have on screen.
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentContainer);
            setupFragment(fragment, fragment.getTag());

            mNavigationDrawer.setSelection(mCurrentTab);
        }
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

    private void initialiseDrawerItems() {
        mDrawerItems = new HashMap<>();
        mDrawerItems.put(R.id.tab_card_db, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_card_db)
                .withName(R.string.card_database)
                .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_database, getTheme())));
        mDrawerItems.put(R.id.tab_public_decks, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_public_decks)
                .withName(R.string.public_decks)
                .withSelectable(BuildConfig.DEBUG)
                .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_public, getTheme())));
        mDrawerItems.put(R.id.tab_decks, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_decks)
                .withName(R.string.deck_builder)
                .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_cards_filled, getTheme())));
        mDrawerItems.put(R.id.tab_collection, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_collection)
                .withName(R.string.collection_manager)
                .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_cards_outline, getTheme())));
        mDrawerItems.put(R.id.tab_results, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_results)
                .withName(R.string.results)
                .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_chart, getTheme())));
        mDrawerItems.put(R.id.tab_helper, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_helper)
                .withSelectable(false)
                .withName(R.string.keg_helper)
                .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_help, getTheme())));
        mDrawerItems.put(R.id.tab_news, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_news)
                .withSelectable(false)
                .withName(R.string.news)
                .withIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_news, getTheme())));
    }

    private void setupFragment(Fragment fragment, String tag) {
        switch (tag) {
            case TAG_CARD_DB:
                mCurrentTab = R.id.tab_card_db;
                break;
            case TAG_PUBLIC_DECKS:
                mCurrentTab = R.id.tab_public_decks;
                break;
            case TAG_COLLECTION:
                mCurrentTab = R.id.tab_collection;
                break;
            case TAG_USER_DECKS:
                mCurrentTab = R.id.tab_decks;
                buttonNewDeck.setVisibility(View.VISIBLE);
                break;
            case TAG_RESULTS_TRACKER:
                mCurrentTab = R.id.tab_results;
                break;
        }
    }

    private void launchFragment(final Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(
                R.id.contentContainer, fragment, tag);
        fragmentTransaction.commit();

        if (fragment.getTag().equals(TAG_USER_DECKS)) {
            buttonNewDeck.setVisibility(View.VISIBLE);
        } else {
            buttonNewDeck.setVisibility(View.GONE);
        }

        // Our options menu will be different for different tabs.
        invalidateOptionsMenu();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_NEWS_SHOWN, newsItemShown);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        if (mCurrentTab == drawerItem.getIdentifier()) {
            return false;
        }

        Fragment fragment;
        String tag;
        switch ((int) drawerItem.getIdentifier()) {
            case R.id.tab_card_db:
                fragment = new CardDatabaseFragment();
                tag = TAG_CARD_DB;
                break;
            case R.id.tab_decks:
                fragment = new CardDatabaseFragment();
                tag = TAG_USER_DECKS;
                break;
            case R.id.tab_collection:
                fragment = new CardDatabaseFragment();
                tag = TAG_COLLECTION;
                break;
            case R.id.tab_results:
                fragment = new ComingSoonFragment();
                tag = TAG_RESULTS_TRACKER;
                break;
            case R.id.tab_public_decks:
                fragment = new CardDatabaseFragment();
                tag = TAG_PUBLIC_DECKS;
                break;
            case R.id.tab_helper:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.keg_helper)
                        .setMessage(R.string.keg_helper_message)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(R.string.go, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String url = getString(R.string.gwentify_helper);
                                showChromeCustomTab(url);
                            }
                        });
                builder.create().show();
                // Return true to not close the navigation drawer.
                return true;
            case R.id.tab_news:
                String url = getString(R.string.news_url);
                showChromeCustomTab(url);
                return true;
            case R.id.action_about:
                Intent about = new Intent(MainActivity.this, BasePreferenceActivity.class);
                about.putExtra(BasePreferenceActivity.EXTRA_PREFERENCE_LAYOUT, R.xml.about);
                about.putExtra(BasePreferenceActivity.EXTRA_PREFERENCE_TITLE, R.string.about);
                startActivity(about);
                // Return true to not close the navigation drawer.
                return true;
            case R.id.action_settings:
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                settings.putExtra(BasePreferenceActivity.EXTRA_PREFERENCE_LAYOUT, R.xml.settings);
                settings.putExtra(BasePreferenceActivity.EXTRA_PREFERENCE_TITLE, R.string.settings);
                startActivity(settings);
                // Return true to not close the navigation drawer.
                return true;
            default:
                // Don't display the item as the selected item.
                return false;
        }

        setupFragment(fragment, tag);
        launchFragment(fragment, tag);
        return false;
    }

    private void showChromeCustomTab(String url) {
        new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.gwentGreen))
                .setShowTitle(true)
                .build()
                .launchUrl(this, Uri.parse(url));
    }
}

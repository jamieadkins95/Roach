package com.jamieadkins.gwent.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.ComingSoonFragment;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.data.Rarity;
import com.jamieadkins.gwent.data.interactor.PatchInteractorFirebase;
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment;
import com.jamieadkins.gwent.filter.FilterableItem;
import com.jamieadkins.gwent.settings.BasePreferenceActivity;
import com.jamieadkins.gwent.settings.SettingsActivity;
import com.jamieadkins.gwent.base.AuthenticationActivity;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.CardFilterListener;
import com.jamieadkins.gwent.card.CardFilterProvider;
import com.jamieadkins.gwent.card.list.CardListFragment;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.card.list.CardsPresenter;
import com.jamieadkins.gwent.collection.CollectionContract;
import com.jamieadkins.gwent.collection.CollectionFragment;
import com.jamieadkins.gwent.collection.CollectionPresenter;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.CollectionInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.deck.list.DecksContract;
import com.jamieadkins.gwent.deck.list.DecksPresenter;
import com.jamieadkins.gwent.deck.list.DeckListFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AuthenticationActivity implements CardFilterProvider,
        Drawer.OnDrawerItemClickListener, FilterBottomSheetDialogFragment.FilterUiListener {
    private static final String TAG_CARD_DB = "com.jamieadkins.gwent.CardDb";
    private static final String TAG_PUBLIC_DECKS = "com.jamieadkins.gwent.PublicDecks";
    private static final String TAG_USER_DECKS = "com.jamieadkins.gwent.UserDecks";
    private static final String TAG_COLLECTION = "com.jamieadkins.gwent.Collection";
    private static final String TAG_RESULTS_TRACKER = "com.jamieadkins.gwent.ResultsTracker";

    private static final String TAG_FILTER_MENU = "com.jamieadkins.gwent.filter.menu";
    private static final String STATE_FILTER_CARD_DB = "com.jamieadkins.gwent.filter.carddb";
    private static final String STATE_FILTER_COLLECTION = "com.jamieadkins.gwent.filter.collection";

    private static final int ACCOUNT_IDENTIFIER = 1000;
    private static final int SIGN_IN_IDENTIFIER = 1001;
    private static final int SIGN_OUT_IDENTIFIER = 1002;
    private static final int NO_LAUNCH_ATTEMPT = -1;

    private DecksPresenter mDecksPresenter;
    private DecksPresenter mPublicDecksPresenter;
    private CardFilterListener mCardFilterListener;
    private CardsPresenter mCardsPresenter;
    private CollectionPresenter mCollectionPresenter;

    private Map<Integer, CardFilter> mCardFilters;
    private FilterBottomSheetDialogFragment mFilterMenu;

    private int mCurrentTab;
    private int mAttemptedToLaunchTab = NO_LAUNCH_ATTEMPT;

    private Drawer mNavigationDrawer;
    private AccountHeader mAccountHeader;
    private ProfileDrawerItem mProfile;
    private Map<Integer, PrimaryDrawerItem> mDrawerItems;

    private final View.OnClickListener signInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startSignInProcess();
        }
    };

    @Override
    public void initialiseContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCardFilters = new HashMap<>();
        if (savedInstanceState != null) {
            mCardFilters.put(R.id.tab_card_db, (CardFilter) savedInstanceState.get(STATE_FILTER_CARD_DB));
            mCardFilters.put(R.id.tab_collection, (CardFilter) savedInstanceState.get(STATE_FILTER_COLLECTION));
        } else {
            mCardFilters.put(R.id.tab_card_db, new CardFilter());
            mCardFilters.put(R.id.tab_collection, new CardFilter());
        }

        mProfile = new ProfileDrawerItem()
                .withIdentifier(ACCOUNT_IDENTIFIER)
                .withEmail(getString(R.string.signed_out))
                .withNameShown(false);

        final ProfileSettingDrawerItem signIn = new ProfileSettingDrawerItem()
                .withIcon(R.drawable.ic_account_circle)
                .withIdentifier(SIGN_IN_IDENTIFIER)
                .withName(getString(R.string.sign_in));

        mAccountHeader = new AccountHeaderBuilder()
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabledForSingleProfile(true)
                .addProfiles(
                        mProfile,
                        signIn)
                .withProfileImagesVisible(false)
                .withActivity(this)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        switch ((int) profile.getIdentifier()) {
                            case SIGN_IN_IDENTIFIER:
                                startSignInProcess();
                                break;
                            case SIGN_OUT_IDENTIFIER:
                                startSignOutProcess();
                                break;
                            case ACCOUNT_IDENTIFIER:
                                // View Account.
                                break;
                        }
                        return false;
                    }
                })
                .build();

        initialiseDrawerItems();
        mNavigationDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar((Toolbar) findViewById(R.id.toolbar))
                .withShowDrawerOnFirstLaunch(true)
                .withAccountHeader(mAccountHeader)
                .addDrawerItems(
                        new SectionDrawerItem()
                                .withName(R.string.gwent)
                                .withDivider(false),
                        mDrawerItems.get(R.id.tab_card_db),
                        mDrawerItems.get(R.id.tab_public_decks),
                        mDrawerItems.get(R.id.tab_helper),
                        new SectionDrawerItem()
                                .withName(R.string.my_stuff)
                                .withDivider(false),
                        mDrawerItems.get(R.id.tab_decks),
                        mDrawerItems.get(R.id.tab_collection),
                        mDrawerItems.get(R.id.tab_results)
                )
                .addStickyDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(R.id.action_settings).
                                withName(R.string.settings)
                                .withIcon(R.drawable.ic_settings)
                                .withSelectable(false),
                        new PrimaryDrawerItem()
                                .withIdentifier(R.id.action_about).
                                withName(R.string.about)
                                .withIcon(R.drawable.ic_info)
                                .withSelectable(false)
                )
                .withOnDrawerItemClickListener(this)
                .build();

        handleDrawerAuthentication();

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

    private void initialiseDrawerItems() {
        mDrawerItems = new HashMap<>();
        mDrawerItems.put(R.id.tab_card_db, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_card_db)
                .withName(R.string.card_database)
                .withIcon(R.drawable.ic_database));
        mDrawerItems.put(R.id.tab_public_decks, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_public_decks)
                .withName(R.string.public_decks)
                .withSelectable(BuildConfig.DEBUG)
                .withIcon(R.drawable.ic_public));
        mDrawerItems.put(R.id.tab_decks, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_decks)
                .withName(R.string.my_decks)
                .withIcon(R.drawable.ic_cards_filled));
        mDrawerItems.put(R.id.tab_collection, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_collection)
                .withName(R.string.my_collection)
                .withIcon(R.drawable.ic_cards_outline));
        mDrawerItems.put(R.id.tab_results, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_results)
                .withName(R.string.results)
                .withIcon(R.drawable.ic_chart));
        mDrawerItems.put(R.id.tab_helper, new PrimaryDrawerItem()
                .withIdentifier(R.id.tab_helper)
                .withSelectable(false)
                .withName(R.string.keg_helper)
                .withIcon(R.drawable.ic_help));
    }

    private void setupFragment(Fragment fragment, String tag) {
        switch (tag) {
            case TAG_CARD_DB:
                mCurrentTab = R.id.tab_card_db;
                mCardsPresenter = new CardsPresenter((CardsContract.View) fragment,
                        CardsInteractorFirebase.getInstance());
                break;
            case TAG_PUBLIC_DECKS:
                mCurrentTab = R.id.tab_public_decks;
                mPublicDecksPresenter =
                        new DecksPresenter(
                                (DecksContract.View) fragment,
                                new DecksInteractorFirebase(),
                                CardsInteractorFirebase.getInstance(),
                                new PatchInteractorFirebase());
                break;
            case TAG_COLLECTION:
                mCurrentTab = R.id.tab_collection;
                mCollectionPresenter = new CollectionPresenter(
                        (CollectionContract.View) fragment,
                        new CollectionInteractorFirebase(),
                        CardsInteractorFirebase.getInstance());
                break;
            case TAG_USER_DECKS:
                mCurrentTab = R.id.tab_decks;
                mDecksPresenter =
                        new DecksPresenter(
                                (DecksContract.View) fragment,
                                new DecksInteractorFirebase(),
                                CardsInteractorFirebase.getInstance(),
                                new PatchInteractorFirebase());
                break;
            case TAG_RESULTS_TRACKER:
                mCurrentTab = R.id.tab_results;
                break;
        }
    }

    private void launchFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(
                R.id.contentContainer, fragment, tag);
        fragmentTransaction.commit();

        mAttemptedToLaunchTab = NO_LAUNCH_ATTEMPT;

        // Our options menu will be different for different tabs.
        invalidateOptionsMenu();
    }

    private void handleDrawerAuthentication() {
        // Reset by removing both.
        mAccountHeader.removeProfileByIdentifier(SIGN_IN_IDENTIFIER);
        mAccountHeader.removeProfileByIdentifier(SIGN_OUT_IDENTIFIER);

        if (isAuthenticated()) {
            mAccountHeader.updateProfile(
                    mProfile.withEmail(getCurrentUser().getEmail()));

            mAccountHeader.addProfiles(
                    new ProfileSettingDrawerItem()
                            .withIdentifier(SIGN_OUT_IDENTIFIER)
                            .withName(getString(R.string.sign_out))
                            .withIcon(R.drawable.ic_account_circle));

            mDrawerItems.get(R.id.tab_collection).withSelectable(true);
            mNavigationDrawer.updateItem(mDrawerItems.get(R.id.tab_collection));
            mDrawerItems.get(R.id.tab_decks).withSelectable(BuildConfig.DEBUG);
            mNavigationDrawer.updateItem(mDrawerItems.get(R.id.tab_decks));
            mDrawerItems.get(R.id.tab_results).withSelectable(BuildConfig.DEBUG);
            mNavigationDrawer.updateItem(mDrawerItems.get(R.id.tab_results));
        } else {
            mAccountHeader.updateProfile(
                    mProfile.withEmail(getString(R.string.signed_out)));

            mAccountHeader.removeProfileByIdentifier(SIGN_OUT_IDENTIFIER);
            mAccountHeader.addProfiles(
                    new ProfileSettingDrawerItem()
                            .withIdentifier(SIGN_IN_IDENTIFIER)
                            .withName(getString(R.string.sign_in))
                            .withIcon(R.drawable.ic_account_circle));

            // If we are currently in an activity that requires authentication, switch to another.
            if (mCurrentTab == R.id.tab_collection ||
                    mCurrentTab == R.id.tab_decks ||
                    mCurrentTab == R.id.tab_results) {
                mNavigationDrawer.setSelection(R.id.tab_card_db);
            }

            mDrawerItems.get(R.id.tab_collection).withSelectable(false);
            mNavigationDrawer.updateItem(mDrawerItems.get(R.id.tab_collection));
            mDrawerItems.get(R.id.tab_decks).withSelectable(false);
            mNavigationDrawer.updateItem(mDrawerItems.get(R.id.tab_decks));
            mDrawerItems.get(R.id.tab_results).withSelectable(false);
            mNavigationDrawer.updateItem(mDrawerItems.get(R.id.tab_results));
        }
    }

    @Override
    protected void onSignedIn() {
        super.onSignedIn();
        invalidateOptionsMenu();
        handleDrawerAuthentication();

        // User tried to access a different tab before they tried to sign in.
        if (mAttemptedToLaunchTab != NO_LAUNCH_ATTEMPT) {
            mNavigationDrawer.setSelection(mAttemptedToLaunchTab);
        }
    }

    @Override
    protected void onSignedOut() {
        super.onSignedOut();
        invalidateOptionsMenu();
        handleDrawerAuthentication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (mCurrentTab == R.id.tab_card_db || mCurrentTab == R.id.tab_collection) {
            inflater.inflate(R.menu.search, menu);

            MenuItem searchMenuItem = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
            searchView.setQueryHint(getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String query) {
                    if (query.equals("")) {
                        // Don't search for everything!
                        mCardFilters.get(mCurrentTab).setSearchQuery(null);
                        return false;
                    }

                    mCardFilters.get(mCurrentTab).setSearchQuery(query);
                    if (mCardFilterListener != null) {
                        mCardFilterListener.onCardFilterUpdated();
                    }

                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    mCardFilters.get(mCurrentTab).setSearchQuery(null);
                    if (mCardFilterListener != null) {
                        mCardFilterListener.onCardFilterUpdated();
                    }
                    return false;
                }
            });

            if (mCardFilters.get(mCurrentTab).getSearchQuery() != null) {
                searchView.setQuery(mCardFilters.get(mCurrentTab).getSearchQuery(), false);
            }

            inflater.inflate(R.menu.card_filters, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<FilterableItem> filterableItems = new ArrayList<>();
        String filteringOn;
        Filterable[] filterItems;
        switch (item.getItemId()) {
            case R.id.filter_reset:
                mCardFilters.get(mCurrentTab).clearFilters();
                if (mCardFilterListener != null) {
                    mCardFilterListener.onCardFilterUpdated();
                }
                return true;
            case R.id.filter_faction:
                filteringOn = getString(R.string.faction);
                filterItems = Faction.ALL_FACTIONS;
                break;
            case R.id.filter_rarity:
                filteringOn = getString(R.string.rarity);
                filterItems = Rarity.ALL_RARITIES;
                break;
            case R.id.filter_type:
                filteringOn = getString(R.string.type);
                filterItems = Type.ALL_TYPES;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        for (Filterable filterable : filterItems) {
            filterableItems.add(new FilterableItem(
                    filterable.getId(),
                    getString(filterable.getName()),
                    getCardFilter().get(filterable.getId())));
        }

        mFilterMenu = FilterBottomSheetDialogFragment
                .newInstance(filteringOn, filterableItems, this);
        mFilterMenu.show(getSupportFragmentManager(), TAG_FILTER_MENU);

        return true;
    }

    @Override
    public void onFilterChanged(String key, boolean checked) {
        mCardFilters.get(mCurrentTab).put(key, checked);
        if (mCardFilterListener != null) {
            mCardFilterListener.onCardFilterUpdated();
        }
    }

    @Override
    public CardFilter getCardFilter() {
        return mCardFilters.get(mCurrentTab);
    }

    @Override
    public void registerCardFilterListener(CardFilterListener listener) {
        mCardFilterListener = listener;
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        if (mCurrentTab == drawerItem.getIdentifier()) {
            return false;
        }

        mAttemptedToLaunchTab = (int) drawerItem.getIdentifier();

        Fragment fragment;
        String tag;
        switch ((int) drawerItem.getIdentifier()) {
            case R.id.tab_card_db:
                fragment = new CardListFragment();
                tag = TAG_CARD_DB;
                break;
            case R.id.tab_decks:
                // Hide this feature in release versions for now.
                if (!BuildConfig.DEBUG) {
                    showSnackbar(String.format(
                            getString(R.string.is_coming_soon),
                            getString(R.string.my_decks)));
                    return false;
                }

                // Stop authenticated only tabs from being selected.
                if (!isAuthenticated()) {
                    showSnackbar(
                            String.format(getString(R.string.sign_in_to_view),
                                    getString(R.string.decks)),
                            getString(R.string.sign_in),
                            signInClickListener);
                    return false;
                }

                // Else, if authenticated.
                fragment = new DeckListFragment();
                tag = TAG_USER_DECKS;
                break;
            case R.id.tab_collection:
                // Hide this feature in release versions for now.
                if (!BuildConfig.DEBUG && !BuildConfig.BETA) {
                    showSnackbar(String.format(
                            getString(R.string.is_coming_soon),
                            getString(R.string.my_collection)));
                    return false;
                }

                // Stop authenticated only tabs from being selected.
                if (!isAuthenticated()) {
                    showSnackbar(
                            String.format(getString(R.string.sign_in_to_view),
                                    getString(R.string.collection)),
                            getString(R.string.sign_in),
                            signInClickListener);
                    return false;
                }

                // Else, if authenticated.
                fragment = new CollectionFragment();
                tag = TAG_COLLECTION;
                break;

            case R.id.tab_results:
                // Hide this feature in release versions for now.
                if (!BuildConfig.DEBUG) {
                    showSnackbar(String.format(
                            getString(R.string.is_coming_soon),
                            getString(R.string.results)));
                    return false;
                }

                // Stop authenticated only tabs from being selected.
                if (!isAuthenticated()) {
                    showSnackbar(
                            String.format(getString(R.string.sign_in_to_view),
                                    getString(R.string.your_results)),
                            getString(R.string.sign_in),
                            signInClickListener);
                    return false;
                }

                // Else, if authenticated.
                fragment = new ComingSoonFragment();
                tag = TAG_RESULTS_TRACKER;
                break;
            case R.id.tab_public_decks:
                // Hide this feature in release versions for now.
                if (!BuildConfig.DEBUG) {
                    showSnackbar(String.format(
                            getString(R.string.are_coming_soon),
                            getString(R.string.public_decks)));
                    return false;
                }

                fragment = DeckListFragment.newInstance(true);
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
                                Intent gwentify = new Intent(Intent.ACTION_VIEW);
                                gwentify.setData(Uri.parse(getString(R.string.gwentify_helper)));
                                startActivity(gwentify);
                            }
                        });
                builder.create().show();
                // Return true to not close the navigation drawer.
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
                showSnackbar(getString(R.string.coming_soon));
                // Don't display the item as the selected item.
                return false;
        }

        setupFragment(fragment, tag);
        launchFragment(fragment, tag);
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_FILTER_CARD_DB, mCardFilters.get(R.id.tab_card_db));
        outState.putParcelable(STATE_FILTER_COLLECTION, mCardFilters.get(R.id.tab_collection));

        if (mFilterMenu != null) {
            mFilterMenu.dismiss();
        }

        super.onSaveInstanceState(outState);
    }
}

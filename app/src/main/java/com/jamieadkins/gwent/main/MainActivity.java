package com.jamieadkins.gwent.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.ComingSoonFragment;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.about.AboutActivity;
import com.jamieadkins.gwent.base.AuthenticationActivity;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.CardFilterListener;
import com.jamieadkins.gwent.card.CardListFragment;
import com.jamieadkins.gwent.card.CardsContract;
import com.jamieadkins.gwent.card.CardsPresenter;
import com.jamieadkins.gwent.collection.CollectionContract;
import com.jamieadkins.gwent.collection.CollectionFragment;
import com.jamieadkins.gwent.collection.CollectionPresenter;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Group;
import com.jamieadkins.gwent.data.Rarity;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.CollectionInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.deck.DecksContract;
import com.jamieadkins.gwent.deck.DecksPresenter;
import com.jamieadkins.gwent.deck.DeckListFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AuthenticationActivity {

    private DecksPresenter mDecksPresenter;
    private CardFilterListener mCardFilterListener;
    private CardsPresenter mCardsPresenter;
    private CollectionPresenter mCollectionPresenter;

    private Map<Integer, CardFilter> mCardFilters;

    private int mCurrentTab;

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

        // Launch Card DB fragment.
        CardListFragment startingFragment = new CardListFragment();
        mCardsPresenter = new CardsPresenter(startingFragment, new CardsInteractorFirebase());
        launchFragment(startingFragment);
        mCardFilterListener = startingFragment;
        mCurrentTab = R.id.tab_card_db;
        mCardFilters = new HashMap<>();
        mCardFilters.put(R.id.tab_card_db, new CardFilter());
        mCardFilters.put(R.id.tab_collection, new CardFilter());

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.tab_card_db:
                                fragment = new CardListFragment();

                                // Create the presenter.
                                mCardsPresenter = new CardsPresenter((CardsContract.View) fragment,
                                        new CardsInteractorFirebase());
                                mCardFilterListener = (CardFilterListener) fragment;
                                break;
                            case R.id.tab_decks:
                                // Hide this feature in release versions for now.
                                if (!BuildConfig.DEBUG) {
                                    showSnackbar(String.format(
                                            getString(R.string.is_coming_soon),
                                            getString(R.string.my_decks)));
                                    // Don't display the item as the selected item.
                                    return false;
                                }

                                // Stop authenticated only tabs from being selected.
                                if (!isAuthenticated()) {
                                    showSnackbar(
                                            String.format(getString(R.string.sign_in_to_view),
                                                    getString(R.string.decks)),
                                            getString(R.string.sign_in),
                                            signInClickListener);

                                    // Don't display the item as the selected item.
                                    return false;
                                }

                                // Else, if authenticated.
                                fragment = new DeckListFragment();

                                // Create the presenter.
                                mDecksPresenter = new DecksPresenter((DecksContract.View) fragment,
                                        new DecksInteractorFirebase());
                                break;
                            case R.id.tab_collection:
                                // Hide this feature in release versions for now.
                                if (!BuildConfig.DEBUG) {
                                    showSnackbar(String.format(
                                            getString(R.string.is_coming_soon),
                                            getString(R.string.my_collection)));
                                    // Don't display the item as the selected item.
                                    return false;
                                }

                                // Stop authenticated only tabs from being selected.
                                if (!isAuthenticated()) {
                                    showSnackbar(
                                            String.format(getString(R.string.sign_in_to_view),
                                                    getString(R.string.collection)),
                                            getString(R.string.sign_in),
                                            signInClickListener);

                                    // Don't display the item as the selected item.
                                    return false;
                                }

                                // Else, if authenticated.
                                fragment = new CollectionFragment();

                                mCollectionPresenter = new CollectionPresenter(
                                        (CollectionContract.View) fragment,
                                        new CollectionInteractorFirebase(),
                                        new CardsInteractorFirebase());
                                mCardFilterListener = (CardFilterListener) fragment;
                                break;

                            case R.id.tab_results:
                                // Hide this feature in release versions for now.
                                if (!BuildConfig.DEBUG) {
                                    showSnackbar(String.format(
                                            getString(R.string.is_coming_soon),
                                            getString(R.string.results)));
                                    // Don't display the item as the selected item.
                                    return false;
                                }

                                // Stop authenticated only tabs from being selected.
                                if (!isAuthenticated()) {
                                    showSnackbar(
                                            String.format(getString(R.string.sign_in_to_view),
                                                    getString(R.string.your_results)),
                                            getString(R.string.sign_in),
                                            signInClickListener);

                                    // Don't display the item as the selected item.
                                    return false;
                                }

                                // Else, if authenticated.
                                fragment = new ComingSoonFragment();
                                break;
                            case R.id.tab_public_decks:
                                // Hide this feature in release versions for now.
                                if (!BuildConfig.DEBUG) {
                                    showSnackbar(String.format(
                                            getString(R.string.are_coming_soon),
                                            getString(R.string.public_decks)));
                                    // Don't display the item as the selected item.
                                    return false;
                                }

                                fragment = new ComingSoonFragment();
                                break;
                            default:
                                showSnackbar(getString(R.string.coming_soon));
                                // Don't display the item as the selected item.
                                return false;
                        }

                        launchFragment(fragment);
                        mCurrentTab = item.getItemId();
                        return true;
                    }
                });
    }

    private void launchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(
                R.id.contentContainer, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

        // Our options menu will be different for different tabs.
        invalidateOptionsMenu();
    }

    @Override
    protected void onSignedIn() {
        super.onSignedIn();
        invalidateOptionsMenu();
    }

    @Override
    protected void onSignedOut() {
        super.onSignedOut();
        invalidateOptionsMenu();

        // If we are currently in an activity that requires authentication, switch to another.
        if (mCurrentTab == R.id.tab_collection ||
                mCurrentTab == R.id.tab_decks ||
                mCurrentTab == R.id.tab_results) {
            // Have to recreate activity since there is no way to dynamically trigger the bottom
            // navigation bar.
            recreate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (mCurrentTab == R.id.tab_card_db || mCurrentTab == R.id.tab_collection) {
            inflater.inflate(R.menu.search, menu);

            MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) myActionMenuItem.getActionView();
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
                    mCardFilterListener.onCardFilterUpdated();
                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    mCardFilters.get(mCurrentTab).setSearchQuery(null);
                    mCardFilterListener.onCardFilterUpdated();
                    return false;
                }
            });

            inflater.inflate(R.menu.card_filters, menu);
            inflater.inflate(R.menu.base_menu, menu);
        }

        if (isAuthenticated()) {
            inflater.inflate(R.menu.signed_in, menu);
        } else {
            if (BuildConfig.DEBUG) {
                inflater.inflate(R.menu.signed_out, menu);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // We may waste this Dialog if it is not a filter item, but it makes for cleaner code.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (item.getItemId()) {
            case R.id.filter_reset:
                mCardFilters.get(mCurrentTab).clearFilters();
                mCardFilterListener.onCardFilterUpdated();
                return true;
            case R.id.filter_faction:
                boolean[] factions = new boolean[mCardFilters.get(mCurrentTab).getFactions().size()];

                for (String key : mCardFilters.get(mCurrentTab).getFactions().keySet()) {
                    factions[Faction.CONVERT_STRING.get(key)] =
                            mCardFilters.get(mCurrentTab).getFactions().get(key);
                }

                builder.setMultiChoiceItems(
                        R.array.factions_array_with_neutral,
                        factions,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean selected) {
                                mCardFilters.get(mCurrentTab)
                                        .getFactions().put(Faction.CONVERT_INT.get(i), selected);
                                mCardFilterListener.onCardFilterUpdated();
                            }
                        });
                break;
            case R.id.filter_rarity:
                boolean[] rarities = new boolean[mCardFilters.get(mCurrentTab).getRarities().size()];

                for (String key : mCardFilters.get(mCurrentTab).getRarities().keySet()) {
                    rarities[Rarity.CONVERT_STRING.get(key)] =
                            mCardFilters.get(mCurrentTab).getRarities().get(key);
                }

                builder.setMultiChoiceItems(
                        R.array.rarity_array,
                        rarities,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean selected) {
                                mCardFilters.get(mCurrentTab).getRarities().put(Rarity.CONVERT_INT.get(i), selected);
                                mCardFilterListener.onCardFilterUpdated();
                            }
                        });
                break;
            case R.id.filter_type:
                boolean[] types = new boolean[mCardFilters.get(mCurrentTab).getTypes().size()];

                for (String key : mCardFilters.get(mCurrentTab).getTypes().keySet()) {
                    types[Group.CONVERT_STRING.get(key)] = mCardFilters.get(mCurrentTab).getTypes().get(key);
                }

                builder.setMultiChoiceItems(
                        R.array.types_array,
                        types,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean selected) {
                                mCardFilters.get(mCurrentTab)
                                        .getTypes().put(Group.CONVERT_INT.get(i), selected);
                                mCardFilterListener.onCardFilterUpdated();
                            }
                        });
                break;
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }

        builder.setPositiveButton(R.string.button_done, null);
        builder.show();
        return true;
    }

    public CardFilter getCardFilter() {
        return mCardFilters.get(mCurrentTab);
    }
}

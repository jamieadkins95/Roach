package com.jamieadkins.gwent.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jamieadkins.gwent.ComingSoonFragment;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.AuthenticationActivity;
import com.jamieadkins.gwent.card.CardListFragment;
import com.jamieadkins.gwent.card.CardsContract;
import com.jamieadkins.gwent.card.CardsPresenter;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.deck.DecksContract;
import com.jamieadkins.gwent.deck.DecksPresenter;
import com.jamieadkins.gwent.deck.DeckListFragment;

public class MainActivity extends AuthenticationActivity {

    private DecksPresenter mDecksPresenter;
    private CardsPresenter mCardsPresenter;

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
        Fragment startingFragment = new CardListFragment();
        mCardsPresenter = new CardsPresenter((CardsContract.View) startingFragment,
                new CardsInteractorFirebase());
        launchFragment(startingFragment);

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
                                break;
                            case R.id.tab_decks:
                                // Stop authenticated only tabs from being selected.
                                if (!isAuthenticated()) {
                                    showSnackbar(
                                            String.format(getString(R.string.sign_in_only),
                                                    getString(R.string.your_decks)),
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
                                // Stop authenticated only tabs from being selected.
                                if (!isAuthenticated()) {
                                    showSnackbar(
                                            String.format(getString(R.string.sign_in_only),
                                                    getString(R.string.your_collections)),
                                            getString(R.string.sign_in),
                                            signInClickListener);

                                    // Don't display the item as the selected item.
                                    return false;
                                }

                                // Else, if authenticated.
                                fragment = new ComingSoonFragment();
                                break;

                            case R.id.tab_results:
                                // Stop authenticated only tabs from being selected.
                                if (!isAuthenticated()) {
                                    showSnackbar(
                                            String.format(getString(R.string.sign_in_only),
                                                    getString(R.string.your_results)),
                                            getString(R.string.sign_in),
                                            signInClickListener);

                                    // Don't display the item as the selected item.
                                    return false;
                                }

                                // Else, if authenticated.
                                fragment = new ComingSoonFragment();
                                break;
                            default:
                                fragment = new ComingSoonFragment();
                                break;
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

        inflater.inflate(R.menu.main_menu, menu);

        if (isAuthenticated()) {
            inflater.inflate(R.menu.signed_in, menu);
        } else {
            inflater.inflate(R.menu.signed_out, menu);
        }

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this,
                        getString(R.string.coming_soon), Toast.LENGTH_LONG)
                        .show();

                // Return false to hide the keyboard.
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }
}

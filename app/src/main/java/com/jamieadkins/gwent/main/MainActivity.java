package com.jamieadkins.gwent.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.jamieadkins.gwent.ComingSoonFragment;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.AuthenticationActivity;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.deck.DecksContract;
import com.jamieadkins.gwent.deck.DecksPresenter;
import com.jamieadkins.gwent.deck.DeckListFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AuthenticationActivity {

    private DecksPresenter mDecksPresenter;
    private int mCurrentTab;

    private final View.OnClickListener signInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isPlayServicesAvailable()) {
                startSignInProcess();
            }
        }
    };

    @Override
    public void initialiseContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_public_decks);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment;
                switch (tabId) {
                    case R.id.tab_decks:
                        // Stop authenticated only tabs from being selected.
                        if (!isAuthenticated()) {
                            bottomBar.selectTabWithId(mCurrentTab);
                            showSnackbar(R.string.sign_in_only, R.string.sign_in, signInClickListener);
                            break;
                        }

                        // Else, if authenticated.
                        mCurrentTab = tabId;
                        fragment = new DeckListFragment();
                        fragmentTransaction.replace(R.id.contentContainer, fragment, "decks");

                        // Create the presenter.
                        mDecksPresenter = new DecksPresenter((DecksContract.View) fragment,
                                new DecksInteractorFirebase());
                        break;
                    case R.id.tab_collection:
                        // Stop authenticated only tabs from being selected.
                        if (!isAuthenticated()) {
                            bottomBar.selectTabWithId(mCurrentTab);
                            showSnackbar(R.string.sign_in_only, R.string.sign_in, signInClickListener);
                            break;
                        }

                        // Else, if authenticated.
                        mCurrentTab = tabId;
                        fragment = new ComingSoonFragment();
                        fragmentTransaction.replace(R.id.contentContainer, fragment, "collection");
                        break;
                    case R.id.tab_public_decks:
                        mCurrentTab = tabId;
                        fragment = new ComingSoonFragment();
                        fragmentTransaction.replace(R.id.contentContainer, fragment, "public");
                        break;
                    case R.id.tab_card_db:
                        mCurrentTab = tabId;
                        fragment = new ComingSoonFragment();
                        fragmentTransaction.replace(R.id.contentContainer, fragment, "cards");
                        break;
                }

                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}

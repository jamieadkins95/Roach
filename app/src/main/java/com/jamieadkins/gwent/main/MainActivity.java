package com.jamieadkins.gwent.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import com.jamieadkins.gwent.CardListActivityFragment;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.LoggedInActivity;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.deck.DecksContract;
import com.jamieadkins.gwent.deck.DecksPresenter;
import com.jamieadkins.gwent.deck.DeckListFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends LoggedInActivity {

    private DecksPresenter mDecksPresenter;

    @Override
    public void initialiseContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment;
                switch (tabId) {
                    case R.id.tab_decks:
                        fragment = new DeckListFragment();
                        fragmentTransaction.replace(R.id.contentContainer, fragment, "decks");

                        // Create the presenter.
                        mDecksPresenter = new DecksPresenter((DecksContract.View) fragment,
                                new DecksInteractorFirebase());
                        break;
                    case R.id.tab_collection:
                        fragment = new CardListActivityFragment();
                        fragmentTransaction.replace(R.id.contentContainer, fragment, "collection");
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

package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.PatchInteractorFirebase;
import com.jamieadkins.gwent.deck.list.DecksPresenter;

/**
 * Created by jamiea on 27/02/17.
 */

public class PublicDeckDetailActivity extends DeckDetailActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseDeckDetailFragment fragment;

        if (savedInstanceState != null) {
            fragment = (BaseDeckDetailFragment)
                    getSupportFragmentManager().findFragmentByTag(TAG_DECK_DETAIL);
        } else {
            fragment = PublicDeckDetailFragment.newInstance(mDeckId);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer, fragment, TAG_DECK_DETAIL)
                    .commit();
        }

        mDeckDetailsPresenter = new DecksPresenter(
                fragment,
                new DecksInteractorFirebase(),
                CardsInteractorFirebase.getInstance(),
                new PatchInteractorFirebase());
    }

}

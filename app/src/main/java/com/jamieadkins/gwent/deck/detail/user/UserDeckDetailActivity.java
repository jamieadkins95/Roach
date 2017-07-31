package com.jamieadkins.gwent.deck.detail.user;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.data.Rarity;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.deck.detail.DeckDetailActivity;
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment;
import com.jamieadkins.gwent.filter.FilterableItem;
import com.jamieadkins.gwent.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamiea on 27/02/17.
 */

public class UserDeckDetailActivity extends DeckDetailActivity
        implements UserDeckDetailFragment.DeckBuilderListener {
    private static final String STATE_DECK_BUILDER_OPEN = "com.jamieadkins.com.gwent.deck.open";

    private boolean mDeckBuilderOpen = false;
    private UserDeckDetailFragment mFragment;

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof UserDeckDetailFragment) {
            mFragment = (UserDeckDetailFragment) fragment;
            mFragment.setDeckBuilderListener(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            onDeckBuilderStateChanged(savedInstanceState.getBoolean(STATE_DECK_BUILDER_OPEN));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDeckBuilderOpen) {
                    mFragment.closeDeckBuilderMenu();
                    return true;
                } else {
                    return super.onOptionsItemSelected(item);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_DECK_BUILDER_OPEN, mDeckBuilderOpen);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mDeckBuilderOpen) {
            mFragment.closeDeckBuilderMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDeckBuilderStateChanged(boolean open) {
        invalidateOptionsMenu();
        mDeckBuilderOpen = open;

        if (mDeckBuilderOpen) {
            getSupportActionBar().setHomeAsUpIndicator(VectorDrawableCompat.create(getResources(), R.drawable.ic_close, getTheme()));
        } else {
            getSupportActionBar().setHomeAsUpIndicator(VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back, getTheme()));
        }
    }
}

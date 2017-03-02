package com.jamieadkins.gwent.deck.detail.user;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
        implements FilterBottomSheetDialogFragment.FilterUiListener,
        UserDeckDetailFragment.DeckBuilderListener {
    private static final String STATE_DECK_BUILDER_OPEN = "com.jamieadkins.com.gwent.deck.open";

    private FilterBottomSheetDialogFragment mFilterMenu;
    private boolean mDeckBuilderOpen = false;
    private String mTitle;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (mDeckBuilderOpen) {
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
                        getCardFilter().setSearchQuery(null);
                        return false;
                    }

                    getCardFilter().setSearchQuery(query);
                    if (getCardFilterListener() != null) {
                        getCardFilterListener().onCardFilterUpdated();
                    }

                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    getCardFilter().setSearchQuery(null);
                    if (getCardFilterListener() != null) {
                        getCardFilterListener().onCardFilterUpdated();
                    }
                    return false;
                }
            });

            if (getCardFilter().getSearchQuery() != null) {
                searchView.setQuery(getCardFilter().getSearchQuery(), false);
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
            case android.R.id.home:
                if (mDeckBuilderOpen) {
                    mFragment.closeDeckBuilderMenu();
                    return true;
                } else {
                    return super.onOptionsItemSelected(item);
                }
            case R.id.filter_reset:
                resetFilters();
                if (mCardFilterListener != null) {
                    mCardFilterListener.onCardFilterUpdated();
                }
                return true;
            case R.id.filter_faction:
                filteringOn = getString(R.string.faction);
                Filterable faction = Faction.NORTHERN_REALMS;
                switch (mFactionId) {
                    case Faction.MONSTERS_ID:
                        faction = Faction.MONSTERS;
                        break;
                    case Faction.NORTHERN_REALMS_ID:
                        faction = Faction.NORTHERN_REALMS;
                        break;
                    case Faction.SCOIATAEL_ID:
                        faction = Faction.SCOIATAEL;
                        break;
                    case Faction.SKELLIGE_ID:
                        faction = Faction.SKELLIGE;
                        break;
                    case Faction.NILFGAARD_ID:
                        faction = Faction.NILFGAARD;
                        break;
                }
                filterItems = new Filterable[] {faction, Faction.NEUTRAL};
                break;
            case R.id.filter_rarity:
                filteringOn = getString(R.string.rarity);
                filterItems = Rarity.ALL_RARITIES;
                break;
            case R.id.filter_type:
                filteringOn = getString(R.string.type);
                filterItems = new Filterable[] {Type.BRONZE, Type.SILVER, Type.GOLD};
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
        mFilterMenu.show(getSupportFragmentManager(), MainActivity.TAG_FILTER_MENU);

        return true;
    }

    @Override
    public void onFilterChanged(String key, boolean checked) {
        getCardFilter().put(key, checked);
        if (mCardFilterListener != null) {
            mCardFilterListener.onCardFilterUpdated();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mFilterMenu != null) {
            mFilterMenu.dismiss();
        }

        outState.putBoolean(STATE_DECK_BUILDER_OPEN, mDeckBuilderOpen);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDeckBuilderStateChanged(boolean open) {
        invalidateOptionsMenu();
        mDeckBuilderOpen = open;

        if (mDeckBuilderOpen) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
    }
}

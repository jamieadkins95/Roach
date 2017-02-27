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

import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.R;

/**
 * Created by jamiea on 27/02/17.
 */

public class UserDeckDetailActivity extends DeckDetailActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    public void initialiseContentView() {
        setContentView(R.layout.activity_user_deck);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new DeckViewPagerAdapter(getSupportFragmentManager(), mDeckId);
        mPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.this_deck)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.card_database)));
        tabLayout.setupWithViewPager(mPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
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
        return true;
    }
}

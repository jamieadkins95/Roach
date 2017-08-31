package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseActivity;
import com.jamieadkins.gwent.deck.detail.user.UserDeckDetailFragment;
import com.jamieadkins.gwent.deck.list.DeckBriefSummaryView;

/**
 * Shows card image and details.
 */

public abstract class DeckDetailActivity extends BaseActivity {
    public static final String EXTRA_DECK_ID = "com.jamieadkins.gwent.deckid";
    public static final String EXTRA_FACTION_ID = "com.jamieadkins.gwent.faction";
    public static final String EXTRA_IS_PUBLIC_DECK = "com.jamieadkins.gwent.public.deck";
    protected static final String TAG_DECK_DETAIL = "com.jamieadkins.gwent.deck.detail";

    private static final String TAG_FRAGMENT = "com.jamieadkins.gwent.deck.detail.fragment";

    protected String mDeckId;
    protected String mFactionId;
    private boolean mIsPublicDeck;

    private DeckBriefSummaryView mSummaryView;

    @Override
    public void initialiseContentView() {
        setContentView(R.layout.activity_deck_detail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSummaryView = (DeckBriefSummaryView) findViewById(R.id.deck_summary_brief);

        Fragment fragment;

        if (savedInstanceState != null) {

            mDeckId = savedInstanceState.getString(EXTRA_DECK_ID);
            mFactionId = savedInstanceState.getString(EXTRA_FACTION_ID);
            mIsPublicDeck = savedInstanceState.getBoolean(EXTRA_IS_PUBLIC_DECK);

            fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        } else {
            mDeckId = getIntent().getStringExtra(EXTRA_DECK_ID);
            mFactionId = getIntent().getStringExtra(EXTRA_FACTION_ID);
            mIsPublicDeck = getIntent().getBooleanExtra(EXTRA_IS_PUBLIC_DECK, false);

            if (mIsPublicDeck) {
                fragment = PublicDeckDetailFragment.newInstance(mDeckId);
            } else {
                fragment = UserDeckDetailFragment.newInstance(mDeckId, mFactionId);
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentContainer, fragment, TAG_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (BuildConfig.DEBUG) {
            inflater.inflate(R.menu.deck_detail, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_publish_deck:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(EXTRA_IS_PUBLIC_DECK, mIsPublicDeck);
        outState.putString(EXTRA_DECK_ID, mDeckId);
        outState.putString(EXTRA_FACTION_ID, mFactionId);
        super.onSaveInstanceState(outState);
    }
}

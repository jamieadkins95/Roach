package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.jamieadkins.commonutils.mvp2.BasePresenter;
import com.jamieadkins.gwent.Injection;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseActivity;
import com.jamieadkins.gwent.data.deck.Deck;
import com.jamieadkins.gwent.deck.detail.user.UserDeckDetailFragment;
import com.jamieadkins.gwent.deck.list.DeckBriefSummaryView;
import com.jamieadkins.gwent.model.GwentFaction;

import org.jetbrains.annotations.NotNull;

import kotlin.NotImplementedError;

/**
 * Shows card image and details.
 */

public abstract class DeckDetailActivity extends BaseActivity implements DeckDetailsContract.DeckSummaryView {
    public static final String EXTRA_DECK_ID = "com.jamieadkins.gwent.deckid";
    public static final String EXTRA_FACTION_ID = "com.jamieadkins.gwent.faction";
    public static final String EXTRA_IS_PUBLIC_DECK = "com.jamieadkins.gwent.public.deck";
    protected static final String TAG_DECK_DETAIL = "com.jamieadkins.gwent.deck.detail";

    private static final String TAG_FRAGMENT = "com.jamieadkins.gwent.deck.detail.fragment";

    protected String mDeckId;
    protected GwentFaction mFaction;
    private boolean mIsPublicDeck;

    private DeckBriefSummaryView mSummaryView;
    private BasePresenter<DeckDetailsContract.DeckSummaryView> presenter;

    protected Fragment fragment;

    @Override
    public void initialiseContentView() {
        setContentView(R.layout.activity_deck_detail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSummaryView = findViewById(R.id.deck_summary_brief);

        if (savedInstanceState != null) {
            mDeckId = savedInstanceState.getString(EXTRA_DECK_ID);
            mFaction = (GwentFaction) savedInstanceState.getSerializable(EXTRA_FACTION_ID);
            mIsPublicDeck = savedInstanceState.getBoolean(EXTRA_IS_PUBLIC_DECK);

            fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        } else {
            mDeckId = getIntent().getStringExtra(EXTRA_DECK_ID);
            mFaction = (GwentFaction) getIntent().getSerializableExtra(EXTRA_FACTION_ID);
            mIsPublicDeck = getIntent().getBooleanExtra(EXTRA_IS_PUBLIC_DECK, false);

            if (mIsPublicDeck) {
                throw new NotImplementedError();
            } else {
                fragment = UserDeckDetailFragment.Companion.newInstance(mDeckId, mFaction);
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentContainer, fragment, TAG_FRAGMENT)
                    .commit();
        }

        presenter = new DeckSummaryPresenter(mDeckId, Injection.INSTANCE.provideDecksInteractor(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onAttach(this);
    }

    @Override
    public void onDeckUpdated(@NotNull Deck deck) {
        setTitle(deck.getName());
        mSummaryView.setDeck(deck);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onDetach();
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
        outState.putSerializable(EXTRA_FACTION_ID, mFaction);
        super.onSaveInstanceState(outState);
    }
}

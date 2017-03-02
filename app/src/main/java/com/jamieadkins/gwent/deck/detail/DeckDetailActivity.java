package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseActivity;
import com.jamieadkins.gwent.base.BaseObserver;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.CardFilterListener;
import com.jamieadkins.gwent.card.CardFilterProvider;
import com.jamieadkins.gwent.card.detail.DetailActivity;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.Filterable;
import com.jamieadkins.gwent.data.Type;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.list.DecksContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Shows card image and details.
 */

public abstract class DeckDetailActivity extends BaseActivity implements CardFilterProvider {
    public static final String EXTRA_DECK_ID = "com.jamieadkins.gwent.deckid";
    public static final String EXTRA_FACTION_ID = "com.jamieadkins.gwent.faction";
    public static final String EXTRA_IS_PUBLIC_DECK = "com.jamieadkins.gwent.public.deck";
    private static final String STATE_CARD_FILTER = "com.jamieadkins.gwent.card.filter";
    protected static final String TAG_DECK_DETAIL = "com.jamieadkins.gwent.deck.detail";

    protected DecksContract.Presenter mDeckDetailsPresenter;
    protected String mDeckId;
    protected String mPatch;
    protected String mFactionId;
    private boolean mIsPublicDeck;

    protected CardFilter mCardFilter;
    protected CardFilterListener mCardFilterListener;

    @Override
    public void initialiseContentView() {
        setContentView(R.layout.activity_preference);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            mCardFilter = (CardFilter) savedInstanceState.get(STATE_CARD_FILTER);
            mDeckId = savedInstanceState.getString(EXTRA_DECK_ID);
            mFactionId = savedInstanceState.getString(EXTRA_FACTION_ID);
            mPatch = savedInstanceState.getString(DetailActivity.EXTRA_PATCH);
            mIsPublicDeck = savedInstanceState.getBoolean(EXTRA_IS_PUBLIC_DECK);
        } else {
            mDeckId = getIntent().getStringExtra(EXTRA_DECK_ID);
            mFactionId = getIntent().getStringExtra(EXTRA_FACTION_ID);
            mPatch = getIntent().getStringExtra(DetailActivity.EXTRA_PATCH);
            mIsPublicDeck = getIntent().getBooleanExtra(EXTRA_IS_PUBLIC_DECK, false);

            mCardFilter = new CardFilter();
            resetFilters();
        }
    }

    protected void resetFilters() {
        mCardFilter.clearFilters();
        mCardFilter.put(Type.LEADER_ID, false);
        mCardFilter.setCollectibleOnly(true);
        for (Filterable faction : Faction.ALL_FACTIONS) {
            if (!faction.getId().equals(mFactionId)) {
                mCardFilter.put(faction.getId(), false);
            }
        }
        mCardFilter.put(Faction.NEUTRAL_ID, true);
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
                mDeckDetailsPresenter.getDeck(mDeckId, mIsPublicDeck)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<RxDatabaseEvent<Deck>>() {
                            @Override
                            public void onNext(RxDatabaseEvent<Deck> value) {
                                mDeckDetailsPresenter.publishDeck(value.getValue());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public CardFilter getCardFilter() {
        return mCardFilter;
    }

    @Override
    public void registerCardFilterListener(CardFilterListener listener) {
        mCardFilterListener = listener;
    }

    protected CardFilterListener getCardFilterListener() {
        return mCardFilterListener;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_CARD_FILTER, mCardFilter);
        outState.putBoolean(EXTRA_IS_PUBLIC_DECK, mIsPublicDeck);
        outState.putString(EXTRA_DECK_ID, mDeckId);
        outState.putString(EXTRA_FACTION_ID, mFactionId);
        outState.putString(DetailActivity.EXTRA_PATCH, mPatch);
        super.onSaveInstanceState(outState);
    }
}

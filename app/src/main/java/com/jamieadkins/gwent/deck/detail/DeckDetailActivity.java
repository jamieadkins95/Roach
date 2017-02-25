package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseActivity;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.CardFilterListener;
import com.jamieadkins.gwent.card.CardFilterProvider;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.card.list.CardsPresenter;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.PatchInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.list.DecksContract;
import com.jamieadkins.gwent.deck.list.DecksPresenter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Shows card image and details.
 */

public class DeckDetailActivity extends BaseActivity implements CardFilterProvider {
    public static final String EXTRA_DECK_ID = "com.jamieadkins.gwent.deckid";
    public static final String EXTRA_IS_PUBLIC_DECK = "com.jamieadkins.gwent.public.deck";
    private static final String STATE_CARD_FILTER = "com.jamieadkins.gwent.card.filter";
    private static final String TAG_DECK_DETAIL = "com.jamieadkins.gwent.deck.detail";

    private DecksContract.Presenter mDeckDetailsPresenter;
    private String mDeckId;
    private boolean mIsPublicDeck;

    private CardFilter mCardFilter;
    private CardFilterListener mCardFilterListener;

    @Override
    public void initialiseContentView() {
        setContentView(R.layout.activity_preference);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BaseDeckDetailFragment fragment;

        if (savedInstanceState != null) {
            mCardFilter = (CardFilter) savedInstanceState.get(STATE_CARD_FILTER);
            mDeckId = savedInstanceState.getString(EXTRA_DECK_ID);
            mIsPublicDeck = savedInstanceState.getBoolean(EXTRA_IS_PUBLIC_DECK);

            fragment = (BaseDeckDetailFragment)
                    getSupportFragmentManager().findFragmentByTag(TAG_DECK_DETAIL);
        } else {
            mCardFilter = new CardFilter();
            mDeckId = getIntent().getStringExtra(EXTRA_DECK_ID);
            mIsPublicDeck = getIntent().getBooleanExtra(EXTRA_IS_PUBLIC_DECK, false);

            if (mIsPublicDeck) {
                fragment = PublicDeckDetailFragment.newInstance(mDeckId);
            } else {
                fragment = UserDeckDetailFragment.newInstance(mDeckId);
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer, fragment, TAG_DECK_DETAIL)
                    .commit();
        }

        mDeckDetailsPresenter = new DecksPresenter(
                fragment,
                new DecksInteractorFirebase(),
                new CardsInteractorFirebase(),
                new PatchInteractorFirebase());
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
                        .subscribe(new Observer<RxDatabaseEvent<Deck>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(RxDatabaseEvent<Deck> value) {
                                mDeckDetailsPresenter.publishDeck(value.getValue());
                            }

                            @Override
                            public void onError(Throwable e) {

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(STATE_CARD_FILTER, mCardFilter);
        outState.putBoolean(EXTRA_IS_PUBLIC_DECK, mIsPublicDeck);
        outState.putString(EXTRA_DECK_ID, mDeckId);
        super.onSaveInstanceState(outState);
    }
}

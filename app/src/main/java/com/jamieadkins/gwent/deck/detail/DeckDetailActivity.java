package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jamieadkins.gwent.BuildConfig;
import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseActivity;
import com.jamieadkins.gwent.base.BaseObserver;
import com.jamieadkins.gwent.base.BaseSingleObserver;
import com.jamieadkins.gwent.card.detail.DetailActivity;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.PatchInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.detail.user.UserDeckDetailFragment;
import com.jamieadkins.gwent.deck.list.DeckBriefSummaryView;
import com.jamieadkins.gwent.deck.list.DecksContract;
import com.jamieadkins.gwent.deck.list.DecksPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Shows card image and details.
 */

public abstract class DeckDetailActivity extends BaseActivity {
    public static final String EXTRA_DECK_ID = "com.jamieadkins.gwent.deckid";
    public static final String EXTRA_FACTION_ID = "com.jamieadkins.gwent.faction";
    public static final String EXTRA_IS_PUBLIC_DECK = "com.jamieadkins.gwent.public.deck";
    protected static final String TAG_DECK_DETAIL = "com.jamieadkins.gwent.deck.detail";

    private static final String TAG_FRAGMENT = "com.jamieadkins.gwent.deck.detail.fragment";

    protected DecksContract.Presenter mDeckDetailsPresenter;
    protected String mDeckId;
    protected String mPatch;
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
            mPatch = savedInstanceState.getString(DetailActivity.EXTRA_PATCH);
            mIsPublicDeck = savedInstanceState.getBoolean(EXTRA_IS_PUBLIC_DECK);

            fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        } else {
            mDeckId = getIntent().getStringExtra(EXTRA_DECK_ID);
            mFactionId = getIntent().getStringExtra(EXTRA_FACTION_ID);
            mPatch = getIntent().getStringExtra(DetailActivity.EXTRA_PATCH);
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

        mDeckDetailsPresenter = new DecksPresenter(
                (DecksContract.View) fragment,
                new DecksInteractorFirebase(),
                CardsInteractorFirebase.getInstance(mPatch),
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
    protected void onStart() {
        super.onStart();
        mDeckDetailsPresenter.getDeck(mDeckId, mIsPublicDeck)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RxDatabaseEvent<Deck>>() {
                    @Override
                    public void onNext(RxDatabaseEvent<Deck> value) {
                        mSummaryView.setDeck(value.getValue());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDeckDetailsPresenter.getLatestPatch()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSingleObserver<String>() {
                    @Override
                    public void onSuccess(String patch) {
                        if (!patch.equals(mPatch)) {
                            showPatchOutOfDateSnackbar(patch);
                        }
                    }
                });
    }

    protected void showPatchOutOfDateSnackbar(String latest) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.coordinator_layout),
                getString(R.string.old_patch),
                Snackbar.LENGTH_LONG);
        snackbar.show();
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
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(EXTRA_IS_PUBLIC_DECK, mIsPublicDeck);
        outState.putString(EXTRA_DECK_ID, mDeckId);
        outState.putString(EXTRA_FACTION_ID, mFactionId);
        outState.putString(DetailActivity.EXTRA_PATCH, mPatch);
        super.onSaveInstanceState(outState);
    }
}

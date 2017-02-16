package com.jamieadkins.gwent.deck.detail;

import android.os.Bundle;
import android.view.MenuItem;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseActivity;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.deck.list.DecksContract;
import com.jamieadkins.gwent.deck.list.DecksPresenter;

/**
 * Shows card image and details.
 */

public class DeckDetailActivity extends BaseActivity {
    public static final String EXTRA_DECK_ID = "com.jamieadkins.gwent.deckid";
    public static final String EXTRA_IS_PUBLIC_DECK = "com.jamieadkins.gwent.public.deck";
    private DecksContract.Presenter mDeckDetailsPresenter;
    private String mDeckId;

    @Override
    public void initialiseContentView() {
        setContentView(R.layout.activity_preference);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDeckId = getIntent().getStringExtra(EXTRA_DECK_ID);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BaseDeckDetailFragment fragment;
        boolean isPublicDeck = getIntent().getBooleanExtra(EXTRA_IS_PUBLIC_DECK, false);

        if (isPublicDeck) {
            fragment = PublicDeckDetailFragment.newInstance(mDeckId);
        } else {
            fragment = UserDeckDetailFragment.newInstance(mDeckId);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentContainer, fragment, fragment.getClass().getSimpleName())
                .commit();

        mDeckDetailsPresenter = new DecksPresenter(
                fragment, new DecksInteractorFirebase(isPublicDeck));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

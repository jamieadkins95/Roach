package com.jamieadkins.gwent.deck.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.jamieadkins.gwent.R;
import com.jamieadkins.gwent.base.BaseActivity;
import com.jamieadkins.gwent.card.detail.DetailContract;
import com.jamieadkins.gwent.card.detail.DetailPresenter;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.gwent.deck.list.DecksContract;
import com.jamieadkins.gwent.deck.list.DecksPresenter;
import com.jamieadkins.gwent.main.MainActivity;

/**
 * Shows card image and details.
 */

public class DeckDetailActivity extends BaseActivity {
    public static final String EXTRA_DECK_ID = "com.jamieadkins.gwent.deckid";
    public static final String EXTRA_PATCH = "com.jamieadkins.gwent.patch";
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

        String patch = getIntent().getStringExtra(EXTRA_PATCH);

        CardsInteractor cardsInteractor;

        if (patch != null) {
            cardsInteractor = new CardsInteractorFirebase(patch);
        } else {
            cardsInteractor = new CardsInteractorFirebase();
        }

        DeckDetailFragment fragment = DeckDetailFragment.newInstance(mDeckId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentContainer, fragment, fragment.getClass().getSimpleName())
                .commit();

        mDeckDetailsPresenter = new DecksPresenter(
                fragment, new DecksInteractorFirebase(true), cardsInteractor);
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

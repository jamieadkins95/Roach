package com.jamieadkins.gwent.deck;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.DecksInteractor;
import com.jamieadkins.gwent.data.interactor.DecksInteractorFirebase;
import com.jamieadkins.jgaw.Faction;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

public class DecksPresenter implements DecksContract.Presenter {
    private final DecksInteractor mDecksInteractor;
    private final DecksContract.View mDecksView;

    public DecksPresenter(@NonNull DecksContract.View decksView,
                          @NonNull DecksInteractor decksInteractor) {
        mDecksInteractor = decksInteractor;
        mDecksInteractor.setPresenter(this);

        mDecksView = decksView;
        mDecksView.setPresenter(this);
    }
    @Override
    public void sendDeckToView(Deck deck) {
        mDecksView.showDeck(deck);
        mDecksView.setLoadingIndicator(false);
    }

    @Override
    public void createNewDeck() {
        mDecksInteractor.createNewDeck(Faction.SKELLIGE, Faction.SKELLIGE);
    }

    @Override
    public void start() {
        mDecksInteractor.requestData();
        mDecksView.setLoadingIndicator(true);
    }

    @Override
    public void stop() {
        mDecksInteractor.stopData();
    }

    @Override
    public void onDeckRemoved(String removedDeckId) {
        mDecksView.removeDeck(removedDeckId);
    }
}

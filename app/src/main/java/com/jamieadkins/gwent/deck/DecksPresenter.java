package com.jamieadkins.gwent.deck;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.DecksInteractor;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

public class DecksPresenter implements DecksContract.Presenter {
    private final DecksInteractor mDecksInteractor;
    private final DecksContract.View mDecksView;

    public DecksPresenter(@NonNull DecksContract.View decksView, @NonNull String userId) {
        mDecksInteractor = new DecksInteractor(this, userId);
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
        mDecksInteractor.createNewDeck();
    }

    @Override
    public void start() {
        mDecksInteractor.requestDecks();
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

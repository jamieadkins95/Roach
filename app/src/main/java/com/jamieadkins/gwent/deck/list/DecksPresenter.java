package com.jamieadkins.gwent.deck.list;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.DecksInteractor;
import com.jamieadkins.gwent.data.interactor.PatchInteractor;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

public class DecksPresenter implements DecksContract.Presenter {
    private final DecksInteractor mDecksInteractor;
    private final PatchInteractor mPatchInteractor;
    private final CardsInteractor mCardsInteractor;
    private final DecksContract.View mDecksView;

    public DecksPresenter(@NonNull DecksContract.View decksView,
                          @NonNull DecksInteractor decksInteractor,
                          @NonNull CardsInteractor cardsInteractor,
                          @NonNull PatchInteractor patchInteractor) {
        mDecksInteractor = decksInteractor;

        mPatchInteractor = patchInteractor;

        mCardsInteractor = cardsInteractor;

        mDecksView = decksView;
        mDecksView.setPresenter(this);
    }

    @Override
    public Single<RxDatabaseEvent<Deck>> createNewDeck(final String name, final String faction,
                                                       final CardDetails leader, String patch) {
        return mDecksInteractor.createNewDeck(name, faction, leader, patch);
    }

    @Override
    public void start() {
        mDecksView.setLoadingIndicator(true);
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getUserDecks() {
        return mDecksInteractor.getUserDecks();
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getPublicDecks() {
        return mDecksInteractor.getFeaturedDecks();
    }

    @Override
    public Single<RxDatabaseEvent<Deck>> getDeck(String deckId, boolean isPublicDeck) {
        return mDecksInteractor.getDeck(deckId, isPublicDeck);
    }

    @Override
    public Single<RxDatabaseEvent<Deck>> getDeckOfTheWeek() {
        return mDecksInteractor.getDeckOfTheWeek();
    }

    @Override
    public void publishDeck(Deck deck) {
        mDecksInteractor.publishDeck(deck);
    }

    @Override
    public void stop() {
        mDecksInteractor.stopData();
    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getCards(CardFilter cardFilter) {
        return mCardsInteractor.getCards(cardFilter);
    }

    @Override
    public void onLoadingComplete() {
        mDecksView.setLoadingIndicator(false);
    }
}

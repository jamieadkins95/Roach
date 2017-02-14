package com.jamieadkins.gwent.deck;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.DecksInteractor;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

public class DecksPresenter implements DecksContract.Presenter {
    private final DecksInteractor mDecksInteractor;
    private final CardsInteractor mCardsInteractor;
    private final DecksContract.View mDecksView;

    public DecksPresenter(@NonNull DecksContract.View decksView,
                          @NonNull DecksInteractor decksInteractor,
                          @NonNull CardsInteractor cardsInteractor) {
        mDecksInteractor = decksInteractor;
        mDecksInteractor.setPresenter(this);

        mCardsInteractor = cardsInteractor;
        mCardsInteractor.setPresenter(this);

        mDecksView = decksView;
        mDecksView.setPresenter(this);
    }

    @Override
    public void createNewDeck(String name, String faction) {
        mDecksInteractor.createNewDeck(name, faction);
    }

    @Override
    public void start() {
        mDecksView.setLoadingIndicator(true);
    }

    @Override
    public Observable<RxDatabaseEvent<Deck>> getDecks() {
        return mDecksInteractor.getDecks();
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

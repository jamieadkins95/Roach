package com.jamieadkins.gwent.card;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.DecksInteractor;
import com.jamieadkins.gwent.deck.DecksContract;

import io.reactivex.Observable;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

public class CardsPresenter implements CardsContract.Presenter {
    private final CardsInteractor mCardsInteractor;
    private final CardsContract.View mCardsView;

    public CardsPresenter(@NonNull CardsContract.View decksView,
                          @NonNull CardsInteractor decksInteractor) {
        mCardsInteractor = decksInteractor;
        mCardsInteractor.setPresenter(this);

        mCardsView = decksView;
        mCardsView.setPresenter(this);
    }
    @Override
    public void sendCardToView(CardDetails card) {
        mCardsView.setLoadingIndicator(false);
    }

    @Override
    public void start() {
        mCardsView.setLoadingIndicator(true);
    }

    @Override
    public void stop() {
        mCardsInteractor.stopData();
    }

    @Override
    public Observable<CardDetails> getMoreCards() {
        return mCardsInteractor.getMoreCards();
    }
}

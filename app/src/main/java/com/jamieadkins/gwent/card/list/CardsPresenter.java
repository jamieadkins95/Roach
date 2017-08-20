package com.jamieadkins.gwent.card.list;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

public class CardsPresenter implements CardsContract.Presenter {
    private final CardsInteractor mCardsInteractor;
    private final CardsContract.View mCardsView;

    public CardsPresenter(@NonNull CardsContract.View cardsView,
                          @NonNull CardsInteractor cardsInteractor) {
        mCardsInteractor = cardsInteractor;

        mCardsView = cardsView;
        mCardsView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getCards(CardFilter filter) {
        return mCardsInteractor.getCards(filter, filter.getSearchQuery(), true);
    }

    @Override
    public Single<RxDatabaseEvent<CardDetails>> getCard(String cardId) {
        return mCardsInteractor.getCard(cardId);
    }
}

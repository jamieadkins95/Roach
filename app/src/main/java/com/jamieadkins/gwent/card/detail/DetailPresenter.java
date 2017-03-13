package com.jamieadkins.gwent.card.detail;

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

public class DetailPresenter implements DetailContract.Presenter {
    private final CardsInteractor mDetailInteractor;
    private final DetailContract.View mDetailView;
    private String mCardId;

    public DetailPresenter(@NonNull DetailContract.View detailView,
                           @NonNull CardsInteractor detailInteractor) {
        mDetailInteractor = detailInteractor;

        mDetailView = detailView;
        mDetailView.setPresenter(this);
    }

    @Override
    public Single<RxDatabaseEvent<CardDetails>> getCard(String cardId) {
        return mDetailInteractor.getCard(cardId);
    }

    @Override
    public void start() {

    }

    @Override
    public void setCardId(String cardId) {
        mCardId = cardId;
    }

    @Override
    public String getCardId() {
        return mCardId;
    }

    @Override
    public void stop() {
        mDetailInteractor.removeListeners();
    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getCards(CardFilter cardFilter) {
        return mDetailInteractor.getCards(cardFilter);
    }

    @Override
    public void reportMistake(String cardId, String description) {
        mDetailInteractor.reportMistake(cardId, description);
    }
}

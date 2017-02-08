package com.jamieadkins.gwent.detail;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.card.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;

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
        mDetailInteractor.setPresenter(this);

        mDetailView = detailView;
        mDetailView.setPresenter(this);
    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getCard(String cardId) {
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
    public void onStop() {
        mDetailInteractor.removeListeners();
    }
}

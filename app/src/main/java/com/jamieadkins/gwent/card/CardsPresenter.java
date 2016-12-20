package com.jamieadkins.gwent.card;

import android.support.annotation.NonNull;

import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.DecksInteractor;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.DecksContract;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    public void start() {

    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> search(String query) {
        mCardsInteractor.resetMorePagesCounter();
        return mCardsInteractor.search(query);
    }

    @Override
    public void stop() {
        mCardsInteractor.resetMorePagesCounter();
    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getMoreCards() {
        return mCardsInteractor.getMoreCards();
    }
}

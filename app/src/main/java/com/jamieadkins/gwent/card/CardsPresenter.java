package com.jamieadkins.gwent.card;

import android.support.annotation.NonNull;

import com.jamieadkins.commonutils.mvp.BaseView;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.Faction;
import com.jamieadkins.gwent.data.interactor.CardsInteractor;
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase;
import com.jamieadkins.gwent.data.interactor.DecksInteractor;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;
import com.jamieadkins.gwent.deck.DecksContract;
import com.twitter.sdk.android.core.models.Card;

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
    private CardsContract.View mCardsView;

    public CardsPresenter(@NonNull CardsInteractor decksInteractor) {
        mCardsInteractor = decksInteractor;
        mCardsInteractor.setPresenter(this);
    }

    public CardsPresenter() {
        mCardsInteractor = new CardsInteractorFirebase();
        mCardsInteractor.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void bindView(CardsContract.View view) {
        mCardsView = view;
    }

    @Override
    public void unbindView() {
        mCardsView = null;
    }

    @Override
    public void stop() {

    }

    @Override
    public Observable<RxDatabaseEvent<CardDetails>> getCards(CardFilter filter) {
        return mCardsInteractor.getCards(filter);
    }
}

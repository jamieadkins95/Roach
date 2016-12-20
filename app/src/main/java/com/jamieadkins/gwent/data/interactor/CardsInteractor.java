package com.jamieadkins.gwent.data.interactor;

import com.jamieadkins.commonutils.mvp.BaseInteractor;
import com.jamieadkins.gwent.card.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;

import io.reactivex.Observable;

/**
 * Card manipulation class.
 */

public interface CardsInteractor extends BaseInteractor<CardsContract.Presenter> {

    Observable<RxDatabaseEvent<CardDetails>> getMoreCards();

    void resetMorePagesCounter();

    Observable<RxDatabaseEvent<CardDetails>> search(String query);

    CardDetails getCard(String id);
}

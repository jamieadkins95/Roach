package com.jamieadkins.gwent.data.interactor;

import com.jamieadkins.commonutils.mvp.BaseInteractor;
import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Card manipulation class.
 */

public interface CardsInteractor extends BaseInteractor<BasePresenter> {

    Observable<RxDatabaseEvent<CardDetails>> getCards(CardFilter filter);

    Single<RxDatabaseEvent<CardDetails>> getCard(String id);

    void reportMistake(String cardid, String description);

    void removeListeners();
}

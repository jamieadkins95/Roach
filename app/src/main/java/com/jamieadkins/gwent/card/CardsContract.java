package com.jamieadkins.gwent.card;

import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.commonutils.mvp.BaseView;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface CardsContract {
    interface View extends BaseView {
        void onCardFilterUpdated();
    }

    interface Presenter extends BasePresenter<View> {
        void stop();

        Observable<RxDatabaseEvent<CardDetails>> getCards(CardFilter cardFilter);
    }
}

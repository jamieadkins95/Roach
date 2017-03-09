package com.jamieadkins.gwent.card.list;

import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.commonutils.mvp.BaseView;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface CardsContract {
    interface View extends BaseView<Presenter> {
        // No additional methods required.
    }

    interface Presenter extends BasePresenter {
        void stop();

        Observable<RxDatabaseEvent<CardDetails>> getCards(CardFilter cardFilter);
    }
}

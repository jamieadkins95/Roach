package com.jamieadkins.gwent.card.detail;

import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.commonutils.mvp.BaseView;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DetailContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends BasePresenter {

        Observable<RxDatabaseEvent<CardDetails>> getCard(String cardId);

        void setCardId(String id);

        String getCardId();

        void onStop();
    }
}

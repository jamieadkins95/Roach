package com.jamieadkins.gwent.detail;

import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.commonutils.mvp.BaseView;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DetailContract {
    interface View extends BaseView {
        // Don't need anything here.
    }

    interface Presenter extends BasePresenter<View> {

        Observable<RxDatabaseEvent<CardDetails>> getCard(String cardId);

        void setCardId(String id);

        String getCardId();
    }
}

package com.jamieadkins.gwent.card.detail;

import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.commonutils.mvp.BaseView;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DetailContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends CardsContract.Presenter {

        Single<RxDatabaseEvent<CardDetails>> getCard(String cardId);

        void setCardId(String id);

        String getCardId();

        Completable reportMistake(String cardId, String description);
    }
}

package com.jamieadkins.gwent.card.detail;

import android.content.Context;

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

        Context getContext();

        void showCard(CardDetails card);
    }

    interface Presenter {
        void reportMistake(String cardId, String description);
    }
}

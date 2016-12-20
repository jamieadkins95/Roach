package com.jamieadkins.gwent.card;

import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.commonutils.mvp.BaseView;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface CardsContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void onSearchResult(ArrayList<CardDetails> searchResults);

        void onSearchClosed();
    }

    interface Presenter extends BasePresenter {
        void stop();

        Observable<RxDatabaseEvent<CardDetails>> getMoreCards();

        Observable<RxDatabaseEvent<CardDetails>> search(String query);
    }
}

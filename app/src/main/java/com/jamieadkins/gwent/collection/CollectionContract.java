package com.jamieadkins.gwent.collection;

import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.commonutils.mvp.BaseView;
import com.jamieadkins.gwent.card.CardFilter;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Collection;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface CollectionContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends BasePresenter {
        void stop();

        Observable<Collection> getCollection();
    }
}

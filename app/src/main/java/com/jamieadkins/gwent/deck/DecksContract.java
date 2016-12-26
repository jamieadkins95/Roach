package com.jamieadkins.gwent.deck;

import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.commonutils.mvp.BaseView;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DecksContract {
    interface View extends BaseView {
        // Don't need anything here.
    }

    interface Presenter extends BasePresenter<View> {
        Observable<RxDatabaseEvent<Deck>> getDecks();

        void stop();

        void createNewDeck(String name, String faction);

        void onLoadingComplete();
    }
}

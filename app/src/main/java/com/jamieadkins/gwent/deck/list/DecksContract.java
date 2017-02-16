package com.jamieadkins.gwent.deck.list;

import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.commonutils.mvp.BaseView;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.data.Deck;
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent;

import io.reactivex.Observable;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DecksContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends BasePresenter {
        Observable<RxDatabaseEvent<Deck>> getDecks();

        Observable<RxDatabaseEvent<Deck>> getDeck(String deckId);

        void stop();

        void createNewDeck(String name, String faction);

        void onLoadingComplete();
    }
}

package com.jamieadkins.gwent.deck;

import com.jamieadkins.commonutils.mvp.BasePresenter;
import com.jamieadkins.commonutils.mvp.BaseView;
import com.jamieadkins.gwent.data.Deck;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DecksContract {
    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showDeck(Deck deck);
    }

    interface Presenter extends BasePresenter {
        void sendDeckToView(Deck deck);

        void requestDecks();

        void createNewDeck();
    }
}

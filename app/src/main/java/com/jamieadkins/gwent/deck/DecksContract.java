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

        void removeDeck(String removedDeckId);
    }

    interface Presenter extends BasePresenter {
        void sendDeckToView(Deck deck);

        void onDeckRemoved(String removedDeckId);

        void stop();

        void createNewDeck(String name, String faction);
    }
}

package com.jamieadkins.gwent.deck.detail.user;

import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.deck.detail.DeckDetailsContract;

import java.util.List;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface UserDeckDetailsContract {
    interface View extends DeckDetailsContract.View {
        void showPotentialLeaders(List<CardDetails> potentialLeaders);
    }

    interface Presenter extends DeckDetailsContract.Presenter {

        void publishDeck();

        void changeLeader(String leaderId);

        void renameDeck(String name);

        void deleteDeck();
    }
}

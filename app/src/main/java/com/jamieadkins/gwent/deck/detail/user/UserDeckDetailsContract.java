package com.jamieadkins.gwent.deck.detail.user;

import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;
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

        void publishDeck(Deck deck);

        void addCardToDeck(String deckId, CardDetails card);

        void removeCardFromDeck(String deckId, CardDetails card);

        void setLeader(Deck deck, CardDetails leader);

        void renameDeck(String deckId, String name);

        void deleteDeck(String deckId);
    }
}

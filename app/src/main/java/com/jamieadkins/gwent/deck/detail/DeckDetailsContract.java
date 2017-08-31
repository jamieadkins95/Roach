package com.jamieadkins.gwent.deck.detail;

import android.support.annotation.NonNull;

import com.jamieadkins.commonutils.mvp2.BaseListView;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DeckDetailsContract {
    interface View extends BaseListView {
        void showDeck(@NonNull Deck deck);
    }

    interface Presenter extends CardsContract.Presenter {

        void publishDeck(Deck deck);

        void addCardToDeck(String deckId, CardDetails card);

        void removeCardFromDeck(String deckId, CardDetails card);

        void setLeader(Deck deck, CardDetails leader);

        void renameDeck(String deckId, String name);

        void deleteDeck(String deckId);
    }
}

package com.jamieadkins.gwent.deck.list;

import android.support.annotation.NonNull;

import com.jamieadkins.commonutils.mvp2.BaseListView;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.data.CardDetails;
import com.jamieadkins.gwent.data.Deck;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DeckListContract {
    interface View extends BaseListView {
        void showDeck(@NonNull Deck deck);

        void showDeckDetails(@NonNull String deckId, @NonNull String factionId);

        void removeDeck(@NonNull Deck deck);
    }

    interface Presenter extends CardsContract.Presenter {
        // Empty.
    }
}

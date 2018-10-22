package com.jamieadkins.gwent.deck.list;

import androidx.annotation.NonNull;

import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.domain.deck.model.GwentDeck;

import java.util.List;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DeckListContract {
    interface View {
        void showDecks(@NonNull List<GwentDeck> decks);

        void showDeckDetails(@NonNull String deckId);

        void showLoadingIndicator(Boolean loading);
    }

    interface Presenter extends CardsContract.Presenter {
        // Empty.
    }
}

package com.jamieadkins.gwent.deck.list;

import androidx.annotation.NonNull;

import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.domain.deck.model.GwentDeckSummary;

import java.util.Collection;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DeckListContract {
    interface View {
        void showDecks(@NonNull Collection<GwentDeckSummary> decks);

        void showDeckDetails(@NonNull String deckId);

        void showLoadingIndicator(Boolean loading);
    }

    interface Presenter extends CardsContract.Presenter {
        // Empty.
    }
}

package com.jamieadkins.gwent.deck.list;

import android.support.annotation.NonNull;

import com.jamieadkins.commonutils.mvp2.BaseListView;
import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.model.GwentDeckSummary;

import java.util.Collection;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DeckListContract {
    interface View extends BaseListView {
        void showDecks(@NonNull Collection<GwentDeckSummary> decks);

        void showDeckDetails(@NonNull String deckId);
    }

    interface Presenter extends CardsContract.Presenter {
        // Empty.
    }
}

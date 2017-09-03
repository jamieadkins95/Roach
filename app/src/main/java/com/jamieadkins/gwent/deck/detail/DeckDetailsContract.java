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
        void onDeckUpdated(@NonNull Deck deck);

        void updateCardCount(String cardId, int count);

        void onCardAdded(CardDetails card);

        void onCardRemoved(CardDetails card);
    }

    interface Presenter extends CardsContract.Presenter {

    }
}

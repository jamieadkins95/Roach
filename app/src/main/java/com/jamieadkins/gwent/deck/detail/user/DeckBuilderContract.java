package com.jamieadkins.gwent.deck.detail.user;

import com.jamieadkins.gwent.card.list.CardsContract;
import com.jamieadkins.gwent.deck.detail.DeckDetailsContract;

/**
 * Specifies the contract between the view and the presenter.
 */

public interface DeckBuilderContract {
    interface View extends DeckDetailsContract.View, CardsContract.View {

    }

    interface Presenter extends DeckDetailsContract.Presenter {

    }
}

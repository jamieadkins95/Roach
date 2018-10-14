package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.gwent.card.list.CardsContract

/**
 * Specifies the contract between the view and the presenter.
 */

interface DeckBuilderContract {
    interface CardDatabaseView : View, CardsContract.View

    interface View

    interface Presenter : CardsContract.Presenter
}

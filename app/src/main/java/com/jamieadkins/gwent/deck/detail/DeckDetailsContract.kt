package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.Deck

/**
 * Specifies the contract between the view and the presenter.
 */

interface DeckDetailsContract {
    interface DeckDetailsView : DeckBuilderContract.View {
        fun onDeckUpdated(deck: Deck)

        fun onCardAdded(card: CardDetails)

        fun onCardRemoved(card: CardDetails)

        fun onLeaderChanged(newLeader: CardDetails)
    }

    interface Presenter : DeckBuilderContract.Presenter
}

package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.Deck

/**
 * Specifies the contract between the view and the presenter.
 */

interface DeckDetailsContract {
    interface DeckDetailsView : DeckSummaryView, DeckBuilderContract.View {
        fun onCardAdded(card: CardDetails)

        fun onLeaderChanged(newLeader: CardDetails)
    }

    interface DeckSummaryView {
        fun onDeckUpdated(deck: Deck)
    }

    interface Presenter : DeckBuilderContract.Presenter
}

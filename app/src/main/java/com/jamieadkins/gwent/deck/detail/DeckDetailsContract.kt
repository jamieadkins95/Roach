package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.gwent.data.card.CardDetails
import com.jamieadkins.gwent.data.deck.Deck
import com.jamieadkins.gwent.model.GwentCard

/**
 * Specifies the contract between the view and the presenter.
 */

interface DeckDetailsContract {
    interface DeckDetailsView : DeckSummaryView, DeckBuilderContract.View {
        fun onCardAdded(card: GwentCard)

        fun onLeaderChanged(newLeader: GwentCard)
    }

    interface DeckSummaryView {
        fun onDeckUpdated(deck: Deck)
    }

    interface Presenter : DeckBuilderContract.Presenter
}

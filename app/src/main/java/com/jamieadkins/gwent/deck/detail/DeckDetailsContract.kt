package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.gwent.model.deck.GwentDeckCardCounts

interface DeckDetailsContract {
    interface DeckDetailsView : DeckBuilderContract.View {
        fun onLeaderChanged(newLeaderId: String)
    }

    interface DeckSummaryView {
        fun onDeckUpdated(cardCounts: GwentDeckCardCounts)
    }

    interface Presenter : DeckBuilderContract.Presenter
}

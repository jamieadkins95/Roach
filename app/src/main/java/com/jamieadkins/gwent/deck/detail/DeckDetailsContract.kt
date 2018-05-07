package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.gwent.domain.deck.model.GwentDeckCardCounts

interface DeckDetailsContract {
    interface DeckDetailsView : DeckBuilderContract.View {
        fun onLeaderChanged(newLeaderId: String)
    }

    interface DeckSummaryView {
        fun onDeckUpdated(cardCounts: GwentDeckCardCounts)
    }

    interface Presenter : DeckBuilderContract.Presenter
}

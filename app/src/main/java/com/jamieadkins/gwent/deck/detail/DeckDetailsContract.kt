package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.gwent.model.GwentCard
import com.jamieadkins.gwent.model.deck.GwentDeckCard
import com.jamieadkins.gwent.model.deck.GwentDeckCardCounts

interface DeckDetailsContract {
    interface DeckDetailsView : DeckBuilderContract.View {
        fun onLeaderChanged(newLeader: GwentCard)

        fun showCardsInDeck(cards: List<GwentDeckCard>)
    }

    interface DeckSummaryView {
        fun onDeckUpdated(cardCounts: GwentDeckCardCounts)
    }

    interface Presenter : DeckBuilderContract.Presenter
}

package com.jamieadkins.gwent.deck.list

import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.gwent.domain.deck.model.GwentDeck

class DeckListController : TypedEpoxyController<List<GwentDeck>>() {

    override fun buildModels(decks: List<GwentDeck>?) {
        decks?.forEach { deck ->
            val model = DeckViewModel_()
                .id(deck.id)
                .deckName(deck.name)
                .leader(deck.leader)

            model.addTo(this)
        }
    }
}
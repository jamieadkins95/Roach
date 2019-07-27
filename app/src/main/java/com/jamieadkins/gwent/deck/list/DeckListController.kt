package com.jamieadkins.gwent.deck.list

import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.bus.GwentDeckClickEvent
import com.jamieadkins.gwent.domain.deck.model.GwentDeck

class DeckListController : TypedEpoxyController<List<GwentDeck>>() {

    override fun buildModels(decks: List<GwentDeck>?) {
        decks?.forEach { deck ->
            val model = DeckViewModel_()
                .id(deck.id)
                .deckName(deck.name)
                .leader(deck.leader)
                .cardCount(deck.totalCardCount)
                .unitCount(deck.unitCount)
                .provisionCost(deck.provisionCost)
                .provisionAllowance(deck.maxProvisionCost)
                .clickListener { _ -> RxBus.post(GwentDeckClickEvent(deck.id)) }

            model.addTo(this)
        }
    }
}
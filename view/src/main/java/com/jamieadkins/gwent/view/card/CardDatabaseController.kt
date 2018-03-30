package com.jamieadkins.gwent.view.card

import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.gwent.core.GwentCard

class CardDatabaseController : TypedEpoxyController<List<GwentCard>>() {

    override fun buildModels(cards: List<GwentCard>?) {
        cards?.forEach { card ->
            card.id?.let {
                GwentCardViewModel_()
                        .id(it)
                        .cardName(card.name ?: "")
                        .cardTooltip(card.tooltip ?: "")
                        .cardCategories(card.categories)
                        .cardImage(card.cardArt?.low)
                        .addTo(this)
            }
        }
    }
}
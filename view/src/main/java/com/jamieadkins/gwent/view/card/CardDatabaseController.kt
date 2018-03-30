package com.jamieadkins.gwent.view.card

import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.gwent.core.GwentCard

class CardDatabaseController : TypedEpoxyController<List<GwentCard>>() {

    override fun buildModels(cards: List<GwentCard>?) {
        cards?.forEach { card ->
            card.id?.let {
                val model = GwentCardViewModel_()
                        .id(it)
                        .cardName(card.name ?: "")
                        .cardTooltip(card.tooltip ?: "")
                        .cardCategories(card.categories)
                        .cardStrength(card.strength)
                        .cardImage(card.cardArt?.low)

                card.faction?.let {
                    model.cardFaction(it)
                }

                card.rarity?.let {
                    model.cardRarity(it)
                }

                model.addTo(this)
            }
        }
    }
}
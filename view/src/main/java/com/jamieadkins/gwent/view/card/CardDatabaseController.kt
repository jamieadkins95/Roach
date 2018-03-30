package com.jamieadkins.gwent.view.card

import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.gwent.core.GwentCard

class CardDatabaseController : TypedEpoxyController<List<GwentCard>>() {

    override fun buildModels(cards: List<GwentCard>?) {
        cards?.forEach {
            GwentCardViewModel_()
                    .cardName(it.name ?: "")
                    .cardTooltip(it.tooltip ?: "")
                    .cardCategories(it.categories)
                    .cardImage(it.cardArt?.medium)
                    .addTo(this)
        }
    }
}
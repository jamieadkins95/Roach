package com.jamieadkins.gwent.view.card

import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed2EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.view.R

class CardDatabaseController : Typed2EpoxyController<List<GwentCard>, Boolean>() {

    @AutoModel lateinit var headerView: HeaderViewModel_

    override fun buildModels(cards: List<GwentCard>?, isSearchResults: Boolean) {

        headerView
                .title(R.string.gwent)
                .addIf(isSearchResults, this)

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
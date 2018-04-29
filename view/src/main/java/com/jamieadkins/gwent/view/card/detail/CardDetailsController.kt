package com.jamieadkins.gwent.view.card.detail

import android.content.res.Resources
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.view.R
import com.jamieadkins.gwent.view.bus.GwentCardClickEvent
import com.jamieadkins.gwent.view.card.GwentCardViewModel_
import com.jamieadkins.gwent.view.card.SubHeaderViewModel_

class CardDetailsController(val resources: Resources) : TypedEpoxyController<GwentCard>() {

    @AutoModel lateinit var tooltipHeader: SubHeaderViewModel_
    @AutoModel lateinit var flavorHeader: SubHeaderViewModel_
    @AutoModel lateinit var relatedCardsHeader: SubHeaderViewModel_

    override fun buildModels(card: GwentCard) {

        tooltipHeader
                .title(R.string.tooltip)
                .addTo(this)

        flavorHeader
                .title(R.string.flavor)
                .addTo(this)

        relatedCardsHeader
                .title(R.string.related)
                .addTo(this)

        val relatedCards = listOf(card)

        relatedCards.forEach { relatedCard ->
            relatedCard.id?.let {
                val model = GwentCardViewModel_()
                        .id(it)
                        .cardName(relatedCard.name ?: "")
                        .cardTooltip(relatedCard.tooltip ?: "")
                        .cardCategories(relatedCard.categories)
                        .cardStrength(relatedCard.strength)
                        .cardImage(relatedCard.cardArt?.low)
                        .clickListener { _ -> RxBus.post(GwentCardClickEvent(it)) }

                relatedCard.faction?.let {
                    model.cardFaction(it)
                }

                relatedCard.rarity?.let {
                    model.cardRarity(it)
                }

                model.addTo(this)
            }
        }
    }
}
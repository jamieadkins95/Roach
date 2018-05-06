package com.jamieadkins.gwent.view.card.detail

import android.content.res.Resources
import android.graphics.Typeface
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.core.GwentFaction
import com.jamieadkins.gwent.view.R
import com.jamieadkins.gwent.view.bus.GwentCardClickEvent
import com.jamieadkins.gwent.view.card.CardResourceHelper
import com.jamieadkins.gwent.view.card.GwentCardViewModel_
import com.jamieadkins.gwent.view.card.SubHeaderViewModel_

class CardDetailsController(val resources: Resources) : TypedEpoxyController<GwentCard>() {

    @AutoModel lateinit var tooltipHeader: SubHeaderViewModel_
    @AutoModel lateinit var tooltip: ElevatedTextViewModel_
    @AutoModel lateinit var flavorHeader: SubHeaderViewModel_
    @AutoModel lateinit var flavor: ElevatedTextViewModel_
    @AutoModel lateinit var categoriesHeader: SubHeaderViewModel_
    @AutoModel lateinit var categories: ElevatedTextViewModel_
    @AutoModel lateinit var relatedCardsHeader: SubHeaderViewModel_
    @AutoModel lateinit var craftHeader: SubHeaderViewModel_
    @AutoModel lateinit var craft: CraftCostViewModel_
    @AutoModel lateinit var millHeader: SubHeaderViewModel_
    @AutoModel lateinit var mill: CraftCostViewModel_

    override fun buildModels(card: GwentCard) {

        val shouldShowTooltip = !card.tooltip.isNullOrEmpty()
        tooltipHeader
                .title(R.string.tooltip)
                .addIf(shouldShowTooltip, this)
        tooltip.text(card.tooltip ?: "")
                .typeface(Typeface.DEFAULT)
                .addIf(shouldShowTooltip, this)

        val shouldShowFlavor = !card.flavor.isNullOrEmpty()
        flavorHeader
                .title(R.string.flavor)
                .addIf(shouldShowFlavor, this)
        flavor.text(card.flavor ?: "")
                .typeface(Typeface.defaultFromStyle(Typeface.ITALIC))
                .addIf(shouldShowFlavor, this)

        val shouldShowCategories = card.categories.isNotEmpty()
        categoriesHeader
                .title(R.string.categories)
                .addIf(shouldShowCategories, this)
        categories.text(card.categories.joinToString())
                .typeface(Typeface.DEFAULT)
                .addIf(shouldShowCategories, this)

        craftHeader
                .title(R.string.craft)
                .addTo(this)
        craft.value(card.craftCost)
                .addTo(this)
        millHeader
                .title(R.string.mill)
                .addTo(this)
        mill.value(card.millValue)
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
                        .cardImage(relatedCard.cardArt?.medium)
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
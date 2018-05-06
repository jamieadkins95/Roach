package com.jamieadkins.gwent.view.card.detail

import android.content.res.Resources
import android.graphics.Typeface
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed2EpoxyController
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.view.R
import com.jamieadkins.gwent.view.bus.GwentCardClickEvent
import com.jamieadkins.gwent.view.card.GwentCardViewModel_
import com.jamieadkins.gwent.view.card.SubHeaderViewModel_

class CardDetailsController(val resources: Resources) : Typed2EpoxyController<GwentCard, List<GwentCard>>() {

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

    override fun buildModels(card: GwentCard, relatedCards: List<GwentCard>) {

        tooltipHeader
                .title(R.string.tooltip)
                .addIf(card.tooltip.isNotEmpty(), this)
        tooltip.text(card.tooltip)
                .typeface(Typeface.DEFAULT)
                .addIf(card.tooltip.isNotEmpty(), this)
        flavorHeader
                .title(R.string.flavor)
                .addIf(card.flavor.isNotEmpty(), this)
        flavor.text(card.flavor)
                .typeface(Typeface.defaultFromStyle(Typeface.ITALIC))
                .addIf(card.flavor.isNotEmpty(), this)

        categoriesHeader
                .title(R.string.categories)
                .addIf(card.categories.isNotEmpty(), this)
        categories.text(card.categories.joinToString())
                .typeface(Typeface.DEFAULT)
                .addIf(card.categories.isNotEmpty(), this)

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
                .addIf(relatedCards.isNotEmpty(), this)

        relatedCards.forEach { relatedCard ->
            val model = GwentCardViewModel_()
                    .id(relatedCard.id)
                    .cardName(relatedCard.name)
                    .cardTooltip(relatedCard.tooltip)
                    .cardCategories(relatedCard.categories)
                    .cardStrength(relatedCard.strength)
                    .cardImage(relatedCard.cardArt?.medium)
                    .cardFaction(relatedCard.faction)
                    .cardRarity(relatedCard.rarity)
                    .clickListener { _ -> RxBus.post(GwentCardClickEvent(relatedCard.id)) }

            model.addTo(this)
        }
    }
}
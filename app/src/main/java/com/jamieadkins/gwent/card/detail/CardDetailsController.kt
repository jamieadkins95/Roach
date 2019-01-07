package com.jamieadkins.gwent.card.detail

import android.content.res.Resources
import android.graphics.Typeface
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed2EpoxyController
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.bus.GwentCardClickEvent
import com.jamieadkins.gwent.card.list.GwentCardViewModel_
import com.jamieadkins.gwent.card.list.SubHeaderViewModel_
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.main.GwentStringHelper

class CardDetailsController(val resources: Resources) : Typed2EpoxyController<GwentCard, List<GwentCard>>() {

    @AutoModel lateinit var tooltipHeader: SubHeaderViewModel_
    @AutoModel lateinit var tooltip: ElevatedTextViewModel_
    @AutoModel lateinit var flavorHeader: SubHeaderViewModel_
    @AutoModel lateinit var flavor: ElevatedTextViewModel_
    @AutoModel lateinit var categoriesHeader: SubHeaderViewModel_
    @AutoModel lateinit var categories: ElevatedTextViewModel_
    @AutoModel lateinit var typeHeader: SubHeaderViewModel_
    @AutoModel lateinit var type: ElevatedTextViewModel_
    @AutoModel lateinit var relatedCardsHeader: SubHeaderViewModel_
    @AutoModel lateinit var craftHeader: SubHeaderViewModel_
    @AutoModel lateinit var craft: CraftCostViewModel_
    @AutoModel lateinit var provisionsHeader: SubHeaderViewModel_
    @AutoModel lateinit var provisions: ElevatedTextViewModel_
    @AutoModel lateinit var extraProvisionsHeader: SubHeaderViewModel_
    @AutoModel lateinit var extraProvisions: ElevatedTextViewModel_
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

        typeHeader
            .title(R.string.type)
            .addTo( this)
        type.text(GwentStringHelper.getTypeString(resources, card.type))
            .typeface(Typeface.DEFAULT)
            .addTo(this)

        val showProvisions = card.colour != GwentCardColour.LEADER && card.provisions > 0
        provisionsHeader
            .title(R.string.provisions)
            .addIf(showProvisions, this)
        provisions.text(card.provisions.toString())
            .typeface(Typeface.DEFAULT)
            .addIf(showProvisions, this)

        val showExtraProvisions = card.colour == GwentCardColour.LEADER
        extraProvisionsHeader
            .title(R.string.extra_provisions)
            .addIf(showExtraProvisions, this)
        extraProvisions.text(card.extraProvisions.toString())
            .typeface(Typeface.DEFAULT)
            .addIf(showExtraProvisions, this)

        val showCraft = card.collectible
        craftHeader.title(R.string.craft).addIf(showCraft, this)
        craft.value(card.craftCost).addIf(showCraft, this)
        millHeader.title(R.string.mill).addIf(showCraft, this)
        mill.value(card.millValue).addIf(showCraft, this)

        relatedCardsHeader
                .title(R.string.related)
                .addIf(relatedCards.isNotEmpty(), this)

        relatedCards.forEach { relatedCard ->
            val model = GwentCardViewModel_()
                    .id(relatedCard.id)
                    .cardName(relatedCard.name)
                    .cardTooltip(relatedCard.tooltip)
                    .cardCategories(relatedCard.categories)
                    .cardProvisions(card.provisions)
                    .cardImage(relatedCard.cardArt?.medium)
                    .cardFaction(relatedCard.faction)
                    .cardRarity(relatedCard.rarity)
                    .clickListener { _ -> RxBus.post(GwentCardClickEvent(relatedCard.id)) }

            model.addTo(this)
        }
    }
}
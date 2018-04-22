package com.jamieadkins.gwent.view.card

import android.content.res.Resources
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed2EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.gwent.core.CardDatabaseResult
import com.jamieadkins.gwent.core.GwentCard
import com.jamieadkins.gwent.view.R

class CardDatabaseController(val resources: Resources) : TypedEpoxyController<CardDatabaseResult>() {

    @AutoModel lateinit var headerView: HeaderViewModel_
    @AutoModel lateinit var subheaderView: SubHeaderViewModel_

    override fun buildModels(result: CardDatabaseResult) {

        headerView
                .title(R.string.search_results)
                .secondaryText(resources.getString(R.string.results_found, result.cards.size, result.searchQuery))
                .addIf(result.searchQuery.isNotEmpty(), this)

        val filtersApplied = 0

        subheaderView
                .title(resources.getString(R.string.filters_applied, filtersApplied))
                .addIf(result.searchQuery.isEmpty() && filtersApplied > 0, this)

        result.cards.forEach { card ->
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
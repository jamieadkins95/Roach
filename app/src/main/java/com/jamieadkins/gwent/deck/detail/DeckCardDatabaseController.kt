package com.jamieadkins.gwent.deck.detail

import android.content.res.Resources
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.Typed2EpoxyController
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.bus.GwentCardClickEvent
import com.jamieadkins.gwent.card.list.GwentCardViewModel_
import com.jamieadkins.gwent.card.list.HeaderViewModel_
import com.jamieadkins.gwent.domain.card.model.GwentCard

class DeckCardDatabaseController(val resources: Resources) : Typed2EpoxyController<List<GwentCard>, String>() {

    @AutoModel lateinit var headerView: HeaderViewModel_

    override fun buildModels(cards: List<GwentCard>, searchQuery: String) {

        headerView
                .title(R.string.search_results)
                .secondaryText(resources.getString(R.string.results_found, cards.size, searchQuery))
                .addIf(searchQuery.isNotEmpty(), this)

        cards.forEach { card ->
            val model = GwentCardViewModel_()
                    .id(card.id)
                    .cardName(card.name)
                    .cardTooltip(card.tooltip)
                    .cardCategories(card.categories)
                    .cardProvisions(card.provisions)
                    .cardImage(card.cardArt.medium)
                    .cardFaction(card.faction)
                    .cardRarity(card.rarity)
                    .clickListener { _ -> RxBus.post(GwentCardClickEvent(card.id)) }

            model.addTo(this)
        }
    }
}
package com.jamieadkins.gwent.card.list

import android.content.res.Resources
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.card.screen.CardDatabaseScreenModel
import com.jamieadkins.gwent.bus.DownloadUpdateClickEvent
import com.jamieadkins.gwent.bus.GwentCardClickEvent

class CardDatabaseController(val resources: Resources) : TypedEpoxyController<CardDatabaseScreenModel>() {

    @AutoModel lateinit var updateView: UpdateAvailableViewModel_
    @AutoModel lateinit var headerView: HeaderViewModel_

    override fun buildModels(data: CardDatabaseScreenModel) {

        data.notices.forEach {
            NoticeViewModel_()
                .id(it.id)
                .title(it.title)
                .addTo(this)
        }

        updateView.clickListener { _ -> RxBus.post(DownloadUpdateClickEvent()) }
                .addIf(data.updateAvailable, this)

        headerView
                .title(R.string.search_results)
                .secondaryText(resources.getString(R.string.results_found, data.cards.size, data.searchQuery))
                .addIf(data.searchQuery.isNotEmpty(), this)

        data.cards.forEach { card ->
            val model = GwentCardViewModel_()
                    .id(card.id)
                    .cardName(card.name)
                    .cardTooltip(card.tooltip)
                    .cardCategories(card.categories)
                    .cardProvisions(card.provisions)
                    .cardImage(card.cardArt?.medium)
                    .cardFaction(card.faction)
                    .cardRarity(card.rarity)
                    .clickListener { _ -> RxBus.post(GwentCardClickEvent(card.id)) }

            model.addTo(this)
        }
    }
}
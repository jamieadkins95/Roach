package com.jamieadkins.gwent.deck.detail

import android.content.res.Resources
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.card.list.GwentCardViewModel_
import com.jamieadkins.gwent.card.list.HeaderViewModel_
import com.jamieadkins.gwent.card.list.SubHeaderViewModel_
import com.jamieadkins.gwent.domain.deck.model.GwentDeck

class DeckController(val resources: Resources) : TypedEpoxyController<GwentDeck>() {

    @AutoModel lateinit var headerView: HeaderViewModel_
    @AutoModel lateinit var leaderSubHeader: SubHeaderViewModel_

    override fun buildModels(deck: GwentDeck) {

        var cardCount = 0
        deck.cards.entries.forEach { cardCount += it.value }

        headerView
            .title(R.string.deck)
            .secondaryText(resources.getString(R.string.cards_in_deck, cardCount, 25))
            .addTo(this)

        leaderSubHeader
            .title(R.string.leader)
            .addTo(this)

        val leader = deck.leader
        GwentCardViewModel_()
            .id(leader.id)
            .cardName(leader.name)
            .cardTooltip(leader.tooltip)
            .cardCategories(leader.categories)
            .cardProvisions(leader.provisions)
            .cardImage(leader.cardArt.medium)
            .cardFaction(leader.faction)
            .cardRarity(leader.rarity)
            .addTo(this)
    }
}
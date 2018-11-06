package com.jamieadkins.gwent.deck.detail

import android.content.res.Resources
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.card.list.GwentCardViewModel_
import com.jamieadkins.gwent.card.list.HeaderViewModel_
import com.jamieadkins.gwent.card.list.SubHeaderViewModel_
import com.jamieadkins.gwent.deck.DeckBuilderEvent
import com.jamieadkins.gwent.deck.DeckBuilderEvents
import com.jamieadkins.gwent.domain.deck.model.GwentDeck

class DeckController(val resources: Resources) : TypedEpoxyController<GwentDeck>() {

    @AutoModel lateinit var headerView: HeaderViewModel_
    @AutoModel lateinit var leaderSubHeader: SubHeaderViewModel_
    @AutoModel lateinit var cardsSubHeader: SubHeaderViewModel_

    override fun buildModels(deck: GwentDeck) {

        var cardCount = 0
        deck.cardCounts.entries.forEach { cardCount += it.value }

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
            .clickListener { _ -> DeckBuilderEvents.post(DeckBuilderEvent.LeaderClick(leader.id)) }
            .longClickListener { _ -> DeckBuilderEvents.post(DeckBuilderEvent.LeaderLongClick(leader.id)); true }
            .addTo(this)

        cardsSubHeader
            .title(R.string.cards)
            .addTo(this)

        deck.cards.values.sortedByDescending { it.provisions }
            .forEach { card ->
                val count = deck.cardCounts[card.id] ?: 0
                DeckCardViewModel_()
                    .id(card.id.toLong())
                    .cardName(card.name)
                    .cardTooltip(card.tooltip)
                    .count(count)
                    .provisionCost(card.provisions)
                    .clickListener { _ -> DeckBuilderEvents.post(DeckBuilderEvent.DeckClick(card.id)) }
                    .longClickListener { _ -> DeckBuilderEvents.post(DeckBuilderEvent.DeckLongClick(card.id)); true }
                    .addTo(this)
            }
    }
}
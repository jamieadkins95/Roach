package com.jamieadkins.gwent.model

import com.jamieadkins.commonutils.ui.RecyclerViewItem
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter

data class GwentDeckSummary(val deck: GwentDeck,
                            val leader: GwentCard,
                            val cardCounts: GwentDeckCardCounts) : RecyclerViewItem {

    override fun equals(other: Any?): Boolean {
        return other is GwentDeckSummary && deck.id == other.deck.id
    }

    override fun getItemType(): Int {
        return GwentRecyclerViewAdapter.TYPE_DECK
    }

    override fun areContentsTheSame(other: RecyclerViewItem): Boolean {
        return other is GwentDeckSummary &&
                deck.name == other.deck.name &&
                deck.leaderId == other.deck.leaderId &&
                deck.faction == other.deck.faction &&
                cardCounts == other.cardCounts
    }
}
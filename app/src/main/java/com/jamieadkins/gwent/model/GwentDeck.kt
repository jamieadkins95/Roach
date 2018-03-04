package com.jamieadkins.gwent.model

import com.jamieadkins.commonutils.ui.RecyclerViewItem
import com.jamieadkins.gwent.base.GwentRecyclerViewAdapter

class GwentDeck : RecyclerViewItem {

    var id: String? = null
    var name: String? = null
    var faction: GwentFaction? = null
    var leaderId: String? = null
    var cards: Map<String, Int> = hashMapOf()

    override fun equals(other: Any?): Boolean {
        return other is GwentDeck && other.id == id
    }

    override fun areContentsTheSame(other: RecyclerViewItem): Boolean {
        return other is GwentDeck && cards == other.cards
    }

    override fun toString(): String {
        return "$name $faction $leaderId"
    }

    override fun getItemType(): Int {
        return GwentRecyclerViewAdapter.TYPE_DECK
    }
}
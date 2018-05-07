package com.jamieadkins.gwent.model.deck

import com.jamieadkins.commonutils.ui.RecyclerViewItem
import com.jamieadkins.gwent.domain.GwentFaction

data class GwentDeck(
        val id: String,
        var name: String? = null,
        var faction: GwentFaction,
        var leaderId: String? = null) : RecyclerViewItem {

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
        return 0
    }
}
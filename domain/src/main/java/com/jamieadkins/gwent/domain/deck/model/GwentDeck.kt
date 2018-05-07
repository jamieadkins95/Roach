package com.jamieadkins.gwent.domain.deck.model

import com.jamieadkins.gwent.domain.GwentFaction

data class GwentDeck(
        val id: String,
        var name: String? = null,
        var faction: GwentFaction,
        var leaderId: String? = null) {

    var cards: Map<String, Int> = hashMapOf()

    override fun equals(other: Any?): Boolean {
        return other is GwentDeck && other.id == id
    }

    override fun toString(): String {
        return "$name $faction $leaderId"
    }
}
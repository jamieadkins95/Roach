package com.jamieadkins.gwent.domain.deck.model

import com.jamieadkins.gwent.domain.GwentFaction

data class GwentDeck(
        val id: String,
        val name: String = "",
        val faction: GwentFaction,
        val leaderId: String? = null,
        val cards: Map<String, Int> = mapOf()) {

    override fun toString(): String {
        return "$name $faction $leaderId"
    }
}
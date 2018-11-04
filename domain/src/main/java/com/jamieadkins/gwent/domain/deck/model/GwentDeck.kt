package com.jamieadkins.gwent.domain.deck.model

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import java.util.*

data class GwentDeck(
    val id: String,
    val name: String = "",
    val faction: GwentFaction,
    val leader: GwentCard,
    val createdAt: Date,
    val cards: Map<String, Int> = mapOf()) {

    override fun toString(): String {
        return "$name $faction ${leader.name}"
    }
}
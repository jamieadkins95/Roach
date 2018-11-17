package com.jamieadkins.gwent.domain.tracker

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import java.util.*

data class DeckTrackerGame(
    val id: String,
    val deck: GwentDeck,
    val opponentFaction: GwentFaction,
    val opponentLeader: GwentCard,
    val cardsPlayed: Map<String, Int> = emptyMap(),
    val cardsRemaining: Map<String, Int> = emptyMap(),
    val opponentCardsPlayed: Map<String, GwentCard> = emptyMap(),
    val opponentCardsPlayedCount: Map<String, Int> = emptyMap()) {

    val totalCardsRemaining: Int = deck.totalCardCount - cardsRemaining.values.sum()
}
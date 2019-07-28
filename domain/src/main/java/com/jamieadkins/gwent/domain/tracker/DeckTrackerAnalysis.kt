package com.jamieadkins.gwent.domain.tracker

import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions

data class DeckTrackerAnalysis(
    val leader: GwentCard,
    val opponentCardsPlayed: List<GwentCard>,
    val opponentProvisionsRemaining: Int,
    val opponentAverageProvisionsRemaining: Float
)
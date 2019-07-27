package com.jamieadkins.gwent.domain.tracker.predictions

import com.jamieadkins.gwent.domain.card.model.GwentCard

data class CardPrediction(
    val card: GwentCard,
    val percentage: Int
)
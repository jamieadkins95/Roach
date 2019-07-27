package com.jamieadkins.gwent.domain.tracker.predictions

data class CardPredictions(
    val similarDecksFound: Int,
    val cards: List<CardPrediction>
)
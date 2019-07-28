package com.jamieadkins.gwent.domain.tracker.predictions

data class CardPredictions(
    val similarDecksFound: List<SimilarDeck>,
    val cards: List<CardPrediction>
)
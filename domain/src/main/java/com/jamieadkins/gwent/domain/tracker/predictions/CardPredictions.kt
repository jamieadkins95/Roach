package com.jamieadkins.gwent.domain.tracker.predictions

data class CardPredictions(
    val showNewPatchWarning: Boolean,
    val similarDecksFound: List<SimilarDeck>,
    val cards: List<CardPrediction>
)
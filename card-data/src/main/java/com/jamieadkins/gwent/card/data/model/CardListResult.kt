package com.jamieadkins.gwent.card.data.model

sealed class CardListResult {
    data class Success(val cards: MutableList<CardDetails>) : CardListResult()

    // Failures.
    object Failed : CardListResult()
}
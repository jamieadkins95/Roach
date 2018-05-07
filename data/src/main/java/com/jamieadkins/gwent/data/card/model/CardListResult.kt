package com.jamieadkins.gwent.data.card.model

sealed class CardListResult {
    data class Success(val cards: MutableList<CardDetails>) : CardListResult()

    // Failures.
    object Failed : CardListResult()
}
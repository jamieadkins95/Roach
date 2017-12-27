package com.jamieadkins.gwent.data.card

sealed class CardListResult {
    data class Success(val cards: MutableList<CardDetails>) : CardListResult()

    // Failures.
    object Failed : CardListResult()
}
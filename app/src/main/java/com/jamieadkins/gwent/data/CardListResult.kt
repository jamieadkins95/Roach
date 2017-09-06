package com.jamieadkins.gwent.data

sealed class CardListResult {
    data class Success(val cards: MutableList<CardDetails>) : CardListResult()

    // Failures.
    object Failed : CardListResult()
}
package com.jamieadkins.gwent.data

import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent

sealed class DatabaseResult {
    object Loading : DatabaseResult()

    object Complete : DatabaseResult()

    data class Error(val message: String) : DatabaseResult()

    data class CardResult(val data: RxDatabaseEvent<CardDetails>)

    data class DeckResult(val data: RxDatabaseEvent<Deck>)
}
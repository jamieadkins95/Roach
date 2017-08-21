package com.jamieadkins.gwent.data

import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent

sealed class InteractorResult {
    object Loading : InteractorResult()

    object Complete : InteractorResult()

    data class Error(val reason: String) : InteractorResult()

    data class Result(val data: RxDatabaseEvent<*>) : InteractorResult()
}
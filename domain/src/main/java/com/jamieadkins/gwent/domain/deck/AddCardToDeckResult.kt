package com.jamieadkins.gwent.domain.deck

sealed class AddCardToDeckResult {

    object Success : AddCardToDeckResult()

    object MaximumReached: AddCardToDeckResult()

    object CantAddLeaders: AddCardToDeckResult()


}
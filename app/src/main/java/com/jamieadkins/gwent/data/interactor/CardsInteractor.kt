package com.jamieadkins.gwent.data.interactor

import com.jamieadkins.commonutils.mvp.BaseInteractor
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.CardListResult

import io.reactivex.Completable
import io.reactivex.Single

/**
 * Card manipulation class.
 */

interface CardsInteractor : BaseInteractor {

    fun getCards(filter: CardFilter): Single<CardListResult>

    fun getCards(filter: CardFilter?, cardIds: List<String>): Single<CardListResult>

    fun getCards(filter: CardFilter?, query: String?): Single<CardListResult>

    fun getCard(id: String): Single<CardDetails>

    fun reportMistake(cardid: String, description: String): Completable

    fun removeListeners()
}

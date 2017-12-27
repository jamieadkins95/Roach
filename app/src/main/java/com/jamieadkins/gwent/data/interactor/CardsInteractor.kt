package com.jamieadkins.gwent.data.interactor

import com.jamieadkins.commonutils.mvp.BaseInteractor
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.CardListResult

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Card manipulation class.
 */

interface CardsInteractor : BaseInteractor {

    fun getAllCards(): Flowable<Collection<CardDetails>>

    fun getCards(filter: CardFilter): Flowable<Collection<CardDetails>>

    fun getCards(filter: CardFilter?, cardIds: List<String>): Flowable<Collection<CardDetails>>

    fun getCards(filter: CardFilter?, query: String?): Flowable<Collection<CardDetails>>

    fun getCard(id: String): Flowable<CardDetails>
}

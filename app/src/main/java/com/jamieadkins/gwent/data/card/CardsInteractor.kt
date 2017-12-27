package com.jamieadkins.gwent.data.card

import com.jamieadkins.commonutils.mvp.BaseInteractor
import com.jamieadkins.gwent.card.CardFilter

import io.reactivex.Flowable

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

package com.jamieadkins.gwent.data.repository

import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.CardDetails
import io.reactivex.Flowable

interface CardsDataSource {

    fun getAllCards(): Flowable<List<CardDetails>>

    fun getCards(filter: CardFilter): Flowable<List<CardDetails>>

    fun getCards(filter: CardFilter?, cardIds: List<String>): Flowable<List<CardDetails>>

    fun getCards(filter: CardFilter?, query: String?): Flowable<List<CardDetails>>

    fun getCard(id: String): Flowable<CardDetails>
}
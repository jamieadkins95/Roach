package com.jamieadkins.gwent.data.interactor

import com.jamieadkins.commonutils.mvp.BaseInteractor
import com.jamieadkins.commonutils.mvp.BasePresenter
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.DatabaseResult

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Card manipulation class.
 */

interface CardsInteractor : BaseInteractor<BasePresenter> {

    fun getCards(filter: CardFilter): Single<MutableList<CardDetails>>

    fun getCards(filter: CardFilter?, cardIds: List<String>): Single<MutableList<CardDetails>>

    fun getCards(filter: CardFilter?, query: String?, useIntelligentSearch: Boolean): Single<MutableList<CardDetails>>

    fun getCard(id: String): Single<CardDetails>

    fun reportMistake(cardid: String, description: String): Completable

    fun removeListeners()

    fun setLocale(locale: String)
}

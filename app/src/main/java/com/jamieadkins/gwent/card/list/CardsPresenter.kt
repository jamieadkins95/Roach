package com.jamieadkins.gwent.card.list

import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent

import io.reactivex.Observable
import io.reactivex.Single

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

class CardsPresenter(private val mCardsView: CardsContract.View,
                     private val mCardsInteractor: CardsInteractor) : CardsContract.Presenter {

    init {
        mCardsView.setPresenter(this)
    }

    override fun start() {

    }

    override fun stop() {

    }

    override fun getCards(filter: CardFilter): Observable<RxDatabaseEvent<CardDetails>> {
        return mCardsInteractor.getCards(filter, filter.searchQuery, true)
    }

    override fun getCard(cardId: String): Single<RxDatabaseEvent<CardDetails>> {
        return mCardsInteractor.getCard(cardId)
    }
}

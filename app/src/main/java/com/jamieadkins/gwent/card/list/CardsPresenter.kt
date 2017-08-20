package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseFilterPresenter
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

class CardsPresenter(private val mCardsInteractor: CardsInteractor) :
        BaseFilterPresenter<CardsContract.View>(), CardsContract.Presenter {

    override fun onAttach(newView: CardsContract.View) {
        super.onAttach(newView)
        onLoadData()
    }

    override fun start() {

    }

    override fun stop() {

    }

    override fun onCardFilterUpdated() {
        onLoadData()
    }

    private fun onLoadData() {
        getCards(cardFilter)
                .applySchedulers()
                .subscribe()
                .addToComposite(disposable)
    }

    override fun getCards(filter: CardFilter): Observable<RxDatabaseEvent<CardDetails>> {
        return mCardsInteractor.getCards(filter, filter.searchQuery, true)
    }

    override fun getCard(cardId: String): Single<RxDatabaseEvent<CardDetails>> {
        return mCardsInteractor.getCard(cardId)
    }
}

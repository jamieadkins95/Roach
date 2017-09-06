package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.data.interactor.DecksInteractor
import com.jamieadkins.gwent.data.*
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent

class DeckSummaryPresenter(private val deckId: String, private val decksInteractor: DecksInteractor) :
        BasePresenter<DeckDetailsContract.DeckSummaryView>(), DeckDetailsContract.Presenter {

    override fun onAttach(newView: DeckDetailsContract.DeckSummaryView) {
        super.onAttach(newView)

        decksInteractor.getDeck(deckId, false, true)
                .applySchedulers()
                .subscribeWith(object : BaseDisposableObserver<RxDatabaseEvent<Deck>>() {
                    override fun onNext(event: RxDatabaseEvent<Deck>) {
                        val deck = event.value
                        view?.onDeckUpdated(deck)
                    }

                })
                .addToComposite(disposable)
    }

    override fun onRefresh() {
        // Do nothing.
    }
}
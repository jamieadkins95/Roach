package com.jamieadkins.gwent.deck.detail.user

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.DecksInteractor
import com.jamieadkins.gwent.card.list.BaseCardsPresenter
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent


/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */
class DeckBuilderPresenter(private val deckId: String, private val factionId: String,
                               private val decksInteractor: DecksInteractor,
                               private val cardsInteractor: CardsInteractor) :
        BaseCardsPresenter<DeckBuilderContract.View>(cardsInteractor), DeckBuilderContract.Presenter {

    override fun onAttach(newView: DeckBuilderContract.View) {
        super.onAttach(newView)
        subscribeToCardUpdates()
    }

    private fun subscribeToCardUpdates() {
        decksInteractor.subscribeToCardCountUpdates(deckId)
                .applySchedulers()
                .subscribeWith(object : BaseDisposableObserver<RxDatabaseEvent<Int>>() {
                    override fun onNext(event: RxDatabaseEvent<Int>) {
                        when (event.eventType) {
                            RxDatabaseEvent.EventType.ADDED -> {
                                cardsInteractor.getCard(event.key)
                                        .applySchedulers()
                                        .subscribe { card ->
                                            view?.updateCardCount(card.ingameId, event.value)
                                        }
                                        .addToComposite(disposable)
                            }
                            RxDatabaseEvent.EventType.REMOVED -> {
                                cardsInteractor.getCard(event.key)
                                        .applySchedulers()
                                        .subscribe { card ->
                                            view?.updateCardCount(card.ingameId, 0)
                                        }
                                        .addToComposite(disposable)
                            }
                            RxDatabaseEvent.EventType.CHANGED -> {
                                view?.updateCardCount(event.key, event.value)
                            }
                        }
                    }

                })
                .addToComposite(disposable)
    }

    override fun onRefresh() {
        super.onRefresh()
        decksInteractor.getDeck(deckId, false)
                .applySchedulers()
                .subscribe()
                .addToComposite(disposable)
    }

    override fun onDetach() {
        super.onDetach()
        decksInteractor.stopData()
    }
}
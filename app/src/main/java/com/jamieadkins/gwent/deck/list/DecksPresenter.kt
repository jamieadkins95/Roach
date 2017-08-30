package com.jamieadkins.gwent.deck.list

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.Deck
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.DecksInteractor
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */
class DecksPresenter(private val decksInteractor: DecksInteractor) :
        BaseFilterPresenter<DecksContract.View>(), DecksContract.Presenter {

    override fun onAttach(newView: DecksContract.View) {
        super.onAttach(newView)
        onRefresh()
    }

    override fun onRefresh() {
        decksInteractor.userDecks
                .applySchedulers()
                .subscribeWith(object : BaseDisposableObserver<RxDatabaseEvent<Deck>>() {
                    override fun onNext(event: RxDatabaseEvent<Deck>) {
                        when (event.eventType) {
                            RxDatabaseEvent.EventType.ADDED,
                            RxDatabaseEvent.EventType.CHANGED -> {
                                view?.showDeck(event.value)
                            }
                            RxDatabaseEvent.EventType.REMOVED -> {
                                view?.removeDeck(event.value)
                            }
                            RxDatabaseEvent.EventType.COMPLETE -> {
                                view?.setLoadingIndicator(false)
                            }
                        }
                    }

                })
                .addToComposite(disposable)
    }

    override fun createNewDeck(name: String, faction: String,
                               leader: CardDetails, patch: String) {
        decksInteractor.createNewDeck(name, faction, leader, patch)
                .applySchedulers()
                .subscribe()
                .addToComposite(disposable)
    }

    override fun publishDeck(deck: Deck) {
        decksInteractor.publishDeck(deck)
    }

    override fun deleteDeck(deckId: String) {
        decksInteractor.deleteDeck(deckId)
    }

    override fun addCardToDeck(deckId: String, card: CardDetails) {
        decksInteractor.addCardToDeck(deckId, card)
    }

    override fun removeCardFromDeck(deckId: String, card: CardDetails) {
        decksInteractor.removeCardFromDeck(deckId, card)
    }

    override fun renameDeck(deckId: String, name: String) {
        decksInteractor.renameDeck(deckId, name)
    }

    override fun setLeader(deck: Deck, leader: CardDetails) {
        decksInteractor.setLeader(deck, leader)
    }

    override fun onCardFilterUpdated() {
        // Do nothing.
    }
}

package com.jamieadkins.gwent.deck.list

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.Deck
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.DecksInteractor
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent

import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

class DecksPresenter(private val decksInteractor: DecksInteractor, cardsInteractor: CardsInteractor) :
        BasePresenter<DecksContract.View>(), DecksContract.Presenter {

    override fun onRefresh() {
        decksInteractor.userDecks
                .applySchedulers()
                .doOnNext { event: RxDatabaseEvent<Deck>? ->
                    event?.value?.let {

                    }
                }
                .subscribe()
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
}

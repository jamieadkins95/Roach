package com.jamieadkins.gwent.deck.detail.user

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.bus.DeckEvent
import com.jamieadkins.gwent.bus.NewDeckRequest
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.Deck
import com.jamieadkins.gwent.data.interactor.DecksInteractor
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent
import com.jamieadkins.gwent.deck.list.DeckListContract

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */
class UserDeckDetailsPresenter(private val deckId: String, private val decksInteractor: DecksInteractor) :
        BaseFilterPresenter<UserDeckDetailsContract.View>(), UserDeckDetailsContract.Presenter {

    override fun onAttach(newView: UserDeckDetailsContract.View) {
        super.onAttach(newView)
        onRefresh()

        RxBus.register(DeckEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<DeckEvent>() {
                    override fun onNext(event: DeckEvent) {
                        when (event.data.event) {
                            DeckEvent.Event.ADD_CARD -> {
                                decksInteractor.addCardToDeck(deckId, event.data.card)
                            }
                            DeckEvent.Event.REMOVE_CARD -> {
                                decksInteractor.removeCardFromDeck(deckId, event.data.card)
                            }
                        }
                    }
                })
                .addToComposite(disposable)
    }

    override fun onRefresh() {
        decksInteractor.getDeck(deckId, false)
                .applySchedulers()
                .subscribe()
                .addToComposite(disposable)
    }

    override fun onCardFilterUpdated() {
        // Do nothing.
    }

    override fun publishDeck(deck: Deck?) {
        decksInteractor.publishDeck(deck)
    }

    override fun addCardToDeck(deckId: String?, card: CardDetails?) {
        decksInteractor.addCardToDeck(deckId, card)
    }

    override fun removeCardFromDeck(deckId: String?, card: CardDetails?) {
        decksInteractor.removeCardFromDeck(deckId, card)
    }

    override fun changeLeader(deckId: String?, leaderId: String?) {
        decksInteractor.setLeader(deckId, leaderId)
    }

    override fun renameDeck(deckId: String?, name: String?) {
        decksInteractor.renameDeck(deckId, name)
    }

    override fun deleteDeck(deckId: String?) {
        decksInteractor.deleteDeck(deckId)
    }
}
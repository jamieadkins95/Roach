package com.jamieadkins.gwent.deck.detail.user

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.bus.DeckEvent
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.DecksInteractor
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.*


/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */
class UserDeckDetailsPresenter(private val deckId: String, private val factionId: String,
                               private val decksInteractor: DecksInteractor,
                               private val cardsInteractor: CardsInteractor) :
        BaseFilterPresenter<UserDeckDetailsContract.View>(), UserDeckDetailsContract.Presenter {

    override fun onAttach(newView: UserDeckDetailsContract.View) {
        super.onAttach(newView)
        onRefresh()
        getPotentialLeaders()

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

    private fun getPotentialLeaders() {
        val cardFilter = CardFilter()

        // Set filter to leaders of this faction only.
        Faction.ALL_FACTIONS
                .filter { it.id != factionId }
                .forEach { cardFilter.put(it.id, false) }
        cardFilter.put(Type.BRONZE_ID, false)
        cardFilter.put(Type.SILVER_ID, false)
        cardFilter.put(Type.GOLD_ID, false)

        cardsInteractor.getCards(cardFilter)
                .applySchedulers()
                .subscribeWith(object : BaseDisposableSingle<CardListResult>() {
                    override fun onSuccess(result: CardListResult) {
                        when (result) {
                            is CardListResult.Success -> view?.showPotentialLeaders(result.cards)
                        }
                    }
                })
                .addToComposite(disposable)
    }

    override fun onCardFilterUpdated() {
        // Do nothing.
    }

    override fun publishDeck() {
        decksInteractor.publishDeck(deckId)
    }

    override fun changeLeader(leaderId: String?) {
        decksInteractor.setLeader(deckId, leaderId)
    }

    override fun renameDeck(name: String?) {
        decksInteractor.renameDeck(deckId, name)
    }

    override fun deleteDeck() {
        decksInteractor.deleteDeck(deckId)
    }
}
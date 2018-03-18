package com.jamieadkins.gwent.deck.detail.user

import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.bus.DeckEvent
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.deck.DeckRepository
import com.jamieadkins.gwent.model.CardColour
import com.jamieadkins.gwent.model.GwentFaction
import com.jamieadkins.gwent.model.GwentCard

class UserDeckDetailsPresenter(private val deckId: String,
                               private val deckRepository: DeckRepository,
                               private val cardRepository: CardRepository,
                               schedulerProvider: BaseSchedulerProvider) :
        BaseFilterPresenter<UserDeckDetailsContract.View>(schedulerProvider), UserDeckDetailsContract.Presenter {

    override fun onAttach(newView: UserDeckDetailsContract.View) {
        super.onAttach(newView)
        onRefresh()
        getPotentialLeaders()

        RxBus.register(DeckEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<DeckEvent>() {
                    override fun onNext(event: DeckEvent) {
                        when (event.data.event) {
                            DeckEvent.Event.ADD_CARD -> {
                                deckRepository.addCardToDeck(deckId, event.data.card.id!!)
                            }
                            DeckEvent.Event.REMOVE_CARD -> {
                                deckRepository.removeCardFromDeck(deckId, event.data.card.id!!)
                            }
                        }
                    }
                })
                .addToComposite(disposable)
    }

    private fun getPotentialLeaders() {
        deckRepository.getDeckFaction(deckId)
                .map { faction ->
                    CardFilter().apply {
                        // Set filter to leaders of this faction only.
                        GwentFaction.values()
                                .filter { it != faction }
                                .forEach { factionFilter[it] = false }
                        colourFilter[CardColour.BRONZE] = false
                        colourFilter[CardColour.SILVER] = false
                        colourFilter[CardColour.GOLD] = false
                    }
                }
                .flatMap { filter -> cardRepository.getCards(filter) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableSingle<Collection<GwentCard>>() {
                    override fun onSuccess(result: Collection<GwentCard>) {
                        view?.showPotentialLeaders(result.toList())
                    }
                })
                .addToComposite(disposable)
    }

    override fun onCardFilterUpdated() {
        // Do nothing.
    }

    override fun changeLeader(leaderId: String) {
        deckRepository.setLeader(deckId, leaderId)
    }

    override fun renameDeck(name: String) {
        deckRepository.renameDeck(deckId, name)
    }

    override fun deleteDeck() {
        deckRepository.deleteDeck(deckId)
    }

    override fun onRefresh() {
        // Do nothing.
    }
}
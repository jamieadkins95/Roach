package com.jamieadkins.gwent.deck.detail.user

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository

class UserDeckDetailsPresenter(private val deckId: String,
                               private val deckRepository: DeckRepository,
                               private val cardRepository: CardRepository,
                               schedulerProvider: BaseSchedulerProvider) :
        BasePresenter<UserDeckDetailsContract.View>(schedulerProvider), UserDeckDetailsContract.Presenter {

    override fun onAttach(newView: UserDeckDetailsContract.View) {
        super.onAttach(newView)
    }

    private fun getPotentialLeaders() {
        /*deckRepository.getDeckFaction(deckId)
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
                .addToComposite(disposable)*/
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
        getPotentialLeaders()
    }
}
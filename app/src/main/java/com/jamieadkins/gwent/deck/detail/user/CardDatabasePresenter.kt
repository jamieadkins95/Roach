package com.jamieadkins.gwent.deck.detail.user

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.DecksInteractor
import com.jamieadkins.gwent.card.list.BaseCardsPresenter
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent
import com.jamieadkins.gwent.deck.detail.DeckBuilderContract

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */
class CardDatabasePresenter(private val deckId: String,
                            private val decksInteractor: DecksInteractor,
                            private val cardsInteractor: CardsInteractor) :
        BaseCardsPresenter<DeckBuilderContract.CardDatabaseView>(cardsInteractor), DeckBuilderContract.Presenter {

    override fun onAttach(newView: DeckBuilderContract.CardDatabaseView) {
        super.onAttach(newView)
        subscribeToCardUpdates()
    }

    private fun subscribeToCardUpdates() {
        decksInteractor.subscribeToCardCountUpdates(deckId)
                .applySchedulers()
                .subscribeWith(object : BaseDisposableObserver<RxDatabaseEvent<Int>>() {
                    override fun onNext(event: RxDatabaseEvent<Int>) {
                        val count = if (event.eventType == RxDatabaseEvent.EventType.REMOVED) 0 else event.value
                        view?.updateCardCount(event.key, count)
                    }

                })
                .addToComposite(disposable)

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
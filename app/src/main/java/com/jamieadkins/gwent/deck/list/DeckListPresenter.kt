package com.jamieadkins.gwent.deck.list

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.bus.NewDeckRequest
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.Deck
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.DecksInteractor
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */
class DeckListPresenter(private val decksInteractor: DecksInteractor) :
        BaseFilterPresenter<DeckListContract.View>(), DeckListContract.Presenter {

    override fun onAttach(newView: DeckListContract.View) {
        super.onAttach(newView)
        onRefresh()

        RxBus.register(NewDeckRequest::class.java)
                .subscribeWith(object : BaseDisposableObserver<NewDeckRequest>() {
                    override fun onNext(newDeckRequest: NewDeckRequest) {
                        val id = decksInteractor.createNewDeck(newDeckRequest.data.name, newDeckRequest.data.faction)
                        view?.showDeckDetails(id, newDeckRequest.data.faction)
                    }
                })
                .addToComposite(disposable)
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

    override fun onCardFilterUpdated() {
        // Do nothing.
    }
}

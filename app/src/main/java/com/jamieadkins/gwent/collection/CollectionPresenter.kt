package com.jamieadkins.gwent.collection

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.bus.CollectionEvent
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.card.list.BaseCardsPresenter
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.CollectionInteractor
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

class CollectionPresenter(private val collectionInteractor: CollectionInteractor, cardsInteractor: CardsInteractor) :
        BaseCardsPresenter<CollectionContract.View>(cardsInteractor), CollectionContract.Presenter {

    override fun onLoadData() {
        super.onLoadData()
        collectionInteractor.collection
                .applySchedulers()
                .subscribeWith(object : BaseDisposableObserver<RxDatabaseEvent<Map<String, Long>>>() {
                    override fun onNext(event: RxDatabaseEvent<Map<String, Long>>) {
                        view?.showCollection(event.key, event.value)
                    }
                })
                .addToComposite(disposable)
    }

    override fun onAttach(newView: CollectionContract.View) {
        super.onAttach(newView)
        RxBus.register(CollectionEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<CollectionEvent>() {
                    override fun onNext(event: CollectionEvent) {
                        when (event.data.event) {
                            CollectionEvent.Event.ADD_CARD -> {
                                collectionInteractor.addCardToCollection(event.data.cardId, event.data.variationId)
                            }
                            CollectionEvent.Event.REMOVE_CARD -> {
                                collectionInteractor.removeCardFromCollection(event.data.cardId, event.data.variationId)
                            }
                        }
                    }
                })
                .addToComposite(disposable)
    }

    override fun onDetach() {
        super.onDetach()
        collectionInteractor.stopCollectionUpdates()
    }
}

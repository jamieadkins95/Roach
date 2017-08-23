package com.jamieadkins.gwent.collection

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.ConnectionChecker
import com.jamieadkins.gwent.card.list.BaseCardsPresenter
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.CollectionInteractor

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

class CollectionPresenter(private val collectionInteractor: CollectionInteractor,
                          cardsInteractor: CardsInteractor, connectionChecker: ConnectionChecker) :
        BaseCardsPresenter<CollectionContract.View>(cardsInteractor, connectionChecker), CollectionContract.Presenter {

    override fun onLoadData() {
        super.onLoadData()
        collectionInteractor.collection
                .applySchedulers()
                .subscribe()
                .addToComposite(disposable)
    }

    override fun addCard(cardId: String, variationId: String) {
        collectionInteractor.addCardToCollection(cardId, variationId)
    }

    override fun removeCard(cardId: String, variationId: String) {
        collectionInteractor.removeCardFromCollection(cardId, variationId)
    }

    override fun onDetach() {
        super.onDetach()
        collectionInteractor.stopCollectionUpdates()
    }
}

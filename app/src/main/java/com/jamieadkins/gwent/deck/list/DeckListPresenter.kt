package com.jamieadkins.gwent.deck.list

import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.bus.NewDeckRequest
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.data.repository.deck.DeckRepository
import com.jamieadkins.gwent.model.GwentDeckSummary

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */
class DeckListPresenter(schedulerProvider: BaseSchedulerProvider,
                        private val deckRepository: DeckRepository) :
        BaseFilterPresenter<DeckListContract.View>(schedulerProvider), DeckListContract.Presenter {

    override fun onAttach(newView: DeckListContract.View) {
        super.onAttach(newView)
        onRefresh()

        RxBus.register(NewDeckRequest::class.java)
                .switchMapSingle { deckRepository.createNewDeck(it.data.name, it.data.faction) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableObserver<String>() {
                    override fun onNext(newDeckId: String) {
                        view?.showDeckDetails(newDeckId)
                    }
                })
                .addToComposite(disposable)
    }

    override fun onRefresh() {
        deckRepository.getDecks()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableSingle<Collection<GwentDeckSummary>>() {
                    override fun onSuccess(decks: Collection<GwentDeckSummary>) {
                        view?.showDecks(decks)
                    }
                })
                .addToComposite(disposable)
    }

    override fun onCardFilterUpdated() {
        // Do nothing.
    }
}

package com.jamieadkins.gwent.deck.list

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.bus.NewDeckRequest
import com.jamieadkins.commonutils.bus.RxBus
import com.jamieadkins.gwent.data.repository.deck.DeckRepository
import com.jamieadkins.gwent.model.deck.GwentDeckSummary

class DeckListPresenter(schedulerProvider: BaseSchedulerProvider,
                        private val deckRepository: DeckRepository) :
        BasePresenter<DeckListContract.View>(schedulerProvider), DeckListContract.Presenter {

    override fun onAttach(newView: DeckListContract.View) {
        super.onAttach(newView)
        onRefresh()

        RxBus.register(NewDeckRequest::class.java)
                .observeOn(schedulerProvider.io())
                .switchMapSingle { deckRepository.createNewDeck(it.data.name, it.data.faction) }
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
                        view?.setLoadingIndicator(false)
                    }
                })
                .addToComposite(disposable)
    }
}

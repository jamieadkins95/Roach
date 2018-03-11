package com.jamieadkins.gwent.deck.detail

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.gwent.base.BaseDisposableSubscriber
import com.jamieadkins.gwent.data.repository.deck.DeckRepository
import com.jamieadkins.gwent.model.deck.GwentDeckCardCounts

class DeckSummaryPresenter(private val deckId: String,
                           private val deckRepository: DeckRepository,
                           schedulerProvider: BaseSchedulerProvider) :
        BasePresenter<DeckDetailsContract.DeckSummaryView>(schedulerProvider), DeckDetailsContract.Presenter {

    override fun onAttach(newView: DeckDetailsContract.DeckSummaryView) {
        super.onAttach(newView)

        deckRepository.getDeckCardCounts(deckId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableSubscriber<GwentDeckCardCounts>() {
                    override fun onNext(deck: GwentDeckCardCounts) {
                        view?.onDeckUpdated(deck)
                    }

                })
                .addToComposite(disposable)
    }

    override fun onRefresh() {
        // Do nothing.
    }
}
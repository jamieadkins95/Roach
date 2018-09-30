package com.jamieadkins.gwent.card.detail

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.domain.card.repository.CardRepository

class DetailPresenter(var cardId: String,
                      private val cardRepository: CardRepository,
                      schedulerProvider: BaseSchedulerProvider) :
        BasePresenter<DetailContract.View>(schedulerProvider), DetailContract.Presenter {

    override fun onRefresh() {
        view?.setLoadingIndicator(true)
        cardRepository.getCard(cardId)
                .switchMap { card ->
                    cardRepository.getCards(card.relatedCards)
                            .map { related ->
                                CardDetailsScreenData(card, related.toList())
                            }
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableObserver<CardDetailsScreenData>() {
                    override fun onNext(result: CardDetailsScreenData) {
                        view?.showScreen(result)
                        view?.setLoadingIndicator(false)
                    }
                })
                .addToComposite(disposable)
    }
}

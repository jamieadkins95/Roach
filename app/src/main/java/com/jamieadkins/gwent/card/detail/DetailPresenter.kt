package com.jamieadkins.gwent.card.detail

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.domain.card.repository.CardRepository

class DetailPresenter(var cardId: String,
                      private val cardRepository: CardRepository,
                      schedulerProvider: BaseSchedulerProvider) :
        BasePresenter<DetailContract.View>(schedulerProvider), DetailContract.Presenter {

    override fun onRefresh() {
        view?.setLoadingIndicator(true)
        cardRepository.getCard(cardId)
                .flatMap { card ->
                    cardRepository.getCards(card.relatedCards)
                            .map { related ->
                                CardDetailsScreenData(card, related.toList())
                            }
                }
                .applySchedulers()
                .subscribeWith(object : BaseDisposableSingle<CardDetailsScreenData>() {
                    override fun onSuccess(result: CardDetailsScreenData) {
                        view?.showScreen(result)
                        view?.setLoadingIndicator(false)
                    }
                })
                .addToComposite(disposable)
    }
}

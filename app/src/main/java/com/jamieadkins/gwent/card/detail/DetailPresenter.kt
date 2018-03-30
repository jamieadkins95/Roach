package com.jamieadkins.gwent.card.detail

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.core.GwentCard

class DetailPresenter(var cardId: String,
                      private val cardRepository: CardRepository,
                      schedulerProvider: BaseSchedulerProvider) :
        BasePresenter<DetailContract.View>(schedulerProvider), DetailContract.Presenter {

    override fun onRefresh() {
        /*cardRepository.getCard(cardId)
                .applySchedulers()
                .subscribeWith(object : BaseDisposableSingle<GwentCard>() {
                    override fun onSuccess(result: GwentCard) {
                        view?.showCard(result)
                    }
                })
                .addToComposite(disposable)*/
    }
}

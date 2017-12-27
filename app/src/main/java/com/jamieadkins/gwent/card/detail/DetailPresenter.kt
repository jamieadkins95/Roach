package com.jamieadkins.gwent.card.detail

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableSubscriber
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.interactor.CardsInteractor

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

class DetailPresenter(private val mDetailInteractor: CardsInteractor, var cardId: String) : BasePresenter<DetailContract.View>(),
        DetailContract.Presenter {
    override fun onAttach(newView: DetailContract.View) {
        super.onAttach(newView)
        onRefresh()
    }

    override fun onRefresh() {
        mDetailInteractor.getCard(cardId)
                .applySchedulers()
                .subscribeWith(object : BaseDisposableSubscriber<CardDetails>() {
                    override fun onNext(result: CardDetails) {
                        view?.showCard(result)
                    }
                })
                .addToComposite(disposable)
    }
}

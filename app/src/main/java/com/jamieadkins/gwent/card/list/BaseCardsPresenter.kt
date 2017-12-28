package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableSubscriber
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.data.card.CardsInteractor
import com.jamieadkins.gwent.model.GwentCard
import java.util.concurrent.TimeoutException

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

abstract class BaseCardsPresenter<T : CardsContract.View>(private val mCardsInteractor: CardsInteractor) :
        BaseFilterPresenter<T>(), CardsContract.Presenter {

    override fun onRefresh() {
        onLoadData()
    }

    override fun onAttach(newView: T) {
        super.onAttach(newView)
        onRefresh()
    }

    override fun onCardFilterUpdated() {
        onLoadData()
    }

    open fun onLoadData() {
        view?.setLoadingIndicator(true)

        mCardsInteractor.getCards(cardFilter, searchQuery)
                .applySchedulers()
                .subscribeWith(object : BaseDisposableSubscriber<Collection<GwentCard>>() {
                    override fun onNext(result: Collection<GwentCard>) {
                        view?.showCards(result.toList())
                        if (result.isEmpty()) {
                            view?.showEmptyView()
                        }
                    }

                    override fun onComplete() {
                        super.onComplete()
                        view?.setLoadingIndicator(false)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        if (e is TimeoutException) {
                            view?.showGenericErrorMessage()
                            view?.setLoadingIndicator(false)
                        }
                    }

                })
                .addToComposite(disposable)
    }
}

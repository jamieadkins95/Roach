package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.data.CardListResult
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import java.util.concurrent.TimeUnit
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
                .timeout(10, TimeUnit.SECONDS)
                .subscribeWith(object : BaseDisposableSingle<CardListResult>() {
                    override fun onSuccess(result: CardListResult) {
                        when (result) {
                            is CardListResult.Success -> {
                                view?.showItems(result.cards)
                                if (result.cards.size == 0) {
                                    view?.showEmptyView()
                                }
                                view?.setLoadingIndicator(false)
                            }
                            CardListResult.Failed -> view?.setLoadingIndicator(false)
                        }
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

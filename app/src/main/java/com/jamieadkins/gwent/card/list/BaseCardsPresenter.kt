package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.ConnectionChecker
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.Result
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
                .subscribeWith(object : BaseDisposableSingle<Result<MutableList<CardDetails>>>() {
                    override fun onSuccess(result: Result<MutableList<CardDetails>>) {
                        result.content?.let {
                            view?.showItems(it)
                        }
                        view?.setLoadingIndicator(false)

                        if (searchQuery != null) {
                            if (result.status == Result.Status.INTELLIGENT_SEARCH_FAILED) {
                                view?.showIntelligentSearchFailure()
                                view?.hideAlgoliaAttribution()
                            } else {
                                view?.showAlgoliaAttribution()
                            }
                        } else {
                            view?.hideAlgoliaAttribution()
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

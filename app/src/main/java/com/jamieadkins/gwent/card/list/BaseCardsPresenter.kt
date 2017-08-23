package com.jamieadkins.gwent.card.list

import android.util.Log
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.ConnectionChecker
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

abstract class BaseCardsPresenter<T : CardsContract.View>(private val mCardsInteractor: CardsInteractor, private val connectionChecker: ConnectionChecker) :
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
        val connected = connectionChecker.isConnectedToInternet()
        if (!connected && searchQuery != null) {
            view?.showIntelligentSearchFailure()
        }
        mCardsInteractor.getCards(cardFilter, searchQuery, connected)
                .applySchedulers()
                .timeout(10, TimeUnit.SECONDS)
                .subscribeWith(object : BaseDisposableSingle<MutableList<CardDetails>>() {
                    override fun onSuccess(t: MutableList<CardDetails>) {
                        view?.let {
                            it.showItems(t)
                            it.setLoadingIndicator(false)
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

package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.base.BaseSingleObserver
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent

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
        mCardsInteractor.getCards(cardFilter, searchQuery, true)
                .applySchedulers()
                .subscribeWith(object : BaseDisposableSingle<MutableList<CardDetails>>() {
                    override fun onSuccess(t: MutableList<CardDetails>) {
                        view?.let {
                            it.showItems(t)
                            it.setLoadingIndicator(false)
                        }
                    }

                })
                .addToComposite(disposable)
    }
}

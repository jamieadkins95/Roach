package com.jamieadkins.gwent.card.list

import android.util.Log
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseFilterPresenter
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
                .subscribe(
                        { event: RxDatabaseEvent<CardDetails>? ->
                            event?.value?.let {
                                view?.showItem(it)
                            }
                        },
                        { e ->

                        },
                        {
                          view?.setLoadingIndicator(false)
                        }
                )
                .addToComposite(disposable)
    }
}

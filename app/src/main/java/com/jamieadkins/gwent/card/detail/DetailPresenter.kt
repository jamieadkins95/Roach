package com.jamieadkins.gwent.card.detail

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.BaseCompletableObserver
import com.jamieadkins.gwent.base.BaseSingleObserver
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.bus.SnackbarBundle
import com.jamieadkins.gwent.bus.SnackbarRequest
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.CardDetails
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

class DetailPresenter(private val mDetailInteractor: CardsInteractor, var cardId: String) : BasePresenter<DetailContract.View>(),
        DetailContract.Presenter {
    override fun onAttach(newView: DetailContract.View) {
        super.onAttach(newView)
        view?.setPresenter(this)

        mDetailInteractor.getCard(cardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSingleObserver<RxDatabaseEvent<CardDetails>>() {
                    override fun onSuccess(result: RxDatabaseEvent<CardDetails>) {
                        view?.showCard(result.value)
                    }
                })
    }

    override fun onDetach() {
        super.onDetach()
        mDetailInteractor.removeListeners()
    }

    override fun reportMistake(cardId: String, description: String) {
        mDetailInteractor.reportMistake(cardId, description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseCompletableObserver() {
                    override fun onComplete() {
                        view?.context?.let {
                            RxBus.post(SnackbarRequest(SnackbarBundle(it.getString(R.string.mistake_reported))))
                        }
                    }
                })
    }
}

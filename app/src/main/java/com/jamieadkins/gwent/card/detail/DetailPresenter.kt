package com.jamieadkins.gwent.card.detail

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.BaseCompletableObserver
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.bus.SnackbarBundle
import com.jamieadkins.gwent.bus.SnackbarRequest
import com.jamieadkins.gwent.data.interactor.CardsInteractor

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
                .applySchedulers()
                .subscribe { result -> view?.showCard(result.value)}
                .addToComposite(disposable)
    }

    override fun onDetach() {
        super.onDetach()
        mDetailInteractor.removeListeners()
    }

    override fun reportMistake(cardId: String, description: String) {
        mDetailInteractor.reportMistake(cardId, description)
                .applySchedulers()
                .subscribe(object : BaseCompletableObserver() {
                    override fun onComplete() {
                        view?.context?.let {
                            RxBus.post(SnackbarRequest(SnackbarBundle(it.getString(R.string.mistake_reported))))
                        }
                    }
                })
    }
}

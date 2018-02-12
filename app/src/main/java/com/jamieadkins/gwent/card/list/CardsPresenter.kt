package com.jamieadkins.gwent.card.list

import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */

class CardsPresenter(schedulerProvider: BaseSchedulerProvider,
                     cardRepository: CardRepository,
                     updateRepository: UpdateRepository) :
        BaseCardsPresenter<CardDatabaseContract.View>(schedulerProvider, cardRepository, updateRepository), CardDatabaseContract.Presenter {

    override fun onLoadData() {
        super.onLoadData()

        updateRepository.isUpdateAvailable()
                .applySchedulers()
                .subscribeWith(object : BaseDisposableSingle<Boolean>() {
                    override fun onSuccess(update: Boolean) {
                        if (update) {
                            view?.showUpdateAvailable()
                        }
                    }
                })
                .addToComposite(disposable)
    }
}

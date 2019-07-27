package com.jamieadkins.gwent.update

import com.jamieadkins.gwent.base.BaseDisposableCompletableObserver
import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import com.jamieadkins.gwent.main.BasePresenter
import javax.inject.Inject

class UpdatePresenter @Inject constructor(
    private val view: UpdateContract.View,
    private val schedulerProvider: SchedulerProvider,
    private val updateRepository: UpdateRepository)
    : BasePresenter(), UpdateContract.Presenter {

    override fun onAttach() {
        updateRepository.performUpdate()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeWith(object : BaseDisposableCompletableObserver() {
                override fun onComplete() {
                    super.onComplete()
                    view.finish()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    view.showError()
                }
            })
            .addToComposite()
    }
}

package com.jamieadkins.gwent.update

import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.gwent.base.BaseDisposableCompletableObserver
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import com.jamieadkins.gwent.main.BasePresenter
import io.reactivex.Completable
import javax.inject.Inject

class UpdatePresenter @Inject constructor(
    private val view: UpdateContract.View,
    private val schedulerProvider: BaseSchedulerProvider,
    private val updateRepository: UpdateRepository)
    : BasePresenter(), UpdateContract.Presenter {

    override fun onAttach() {
        updateRepository.performUpdate()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribeWith(object : BaseDisposableCompletableObserver() {
                override fun onComplete() {
                    super.onComplete()
                    view.openCardDatabase()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    view.showError()
                    view.openCardDatabase()
                }
            })
            .addToComposite()
    }
}

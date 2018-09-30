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
        updateRepository.isUpdateAvailable()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.io())
            .flatMapCompletable { update ->
                if (update) {
                    updateRepository.performUpdate()
                } else {
                    Completable.complete()
                }
            }
            .observeOn(schedulerProvider.ui())
            .subscribeWith(object : BaseDisposableCompletableObserver() {
                override fun onComplete() {
                    super.onComplete()
                    view.openCardDatabase()
                }
            })
            .addToComposite()
    }
}

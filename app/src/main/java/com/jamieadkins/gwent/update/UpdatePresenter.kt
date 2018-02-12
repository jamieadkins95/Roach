package com.jamieadkins.gwent.update

import com.jamieadkins.commonutils.mvp2.BasePresenter
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.gwent.base.BaseDisposableCompletableObserver
import com.jamieadkins.gwent.data.repository.update.UpdateRepository
import com.jamieadkins.gwent.model.patch.UpdateState
import io.reactivex.Completable

class UpdatePresenter(schedulerProvider: BaseSchedulerProvider, val updateRepository: UpdateRepository) :
        BasePresenter<UpdateContract.View>(schedulerProvider), UpdateContract.Presenter {

    override fun onRefresh() {
        view?.showUpdateState(UpdateState.CHECKING_FOR_UPDATE)
        updateRepository.isUpdateAvailable()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSuccess { update -> if (update) view?.showUpdateState(UpdateState.UPDATING_DATABASE) }
                .observeOn(schedulerProvider.io())
                .flatMapCompletable { update -> if (update) updateRepository.performUpdate() else Completable.complete() }
                .observeOn(schedulerProvider.ui())
                .subscribeWith(object : BaseDisposableCompletableObserver() {
                    override fun onComplete() {
                        super.onComplete()
                        view?.openCardDatabase()
                    }
                })
                .addToComposite(disposable)
    }
}

package com.jamieadkins.commonutils.mvp2

import com.jamieadkins.commonutils.bus.RefreshEvent
import com.jamieadkins.commonutils.bus.RxBus
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V>(val schedulerProvider: BaseSchedulerProvider) {
    var view: V? = null
    val disposable: CompositeDisposable = CompositeDisposable()

    open fun onAttach(newView: V) {
        view = newView
        onRefresh()
        RxBus.post(RefreshEvent())
    }

    open fun onDetach() {
        view = null
        disposable.clear()
    }

    open fun onRefresh() {}
}
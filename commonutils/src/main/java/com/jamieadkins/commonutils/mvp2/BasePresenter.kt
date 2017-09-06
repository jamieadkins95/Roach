package com.jamieadkins.commonutils.mvp2

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V> {
    var view: V? = null
    val disposable: CompositeDisposable = CompositeDisposable()

    open fun onAttach(newView: V) {
        view = newView
    }

    open fun onDetach() {
        view = null
        disposable.clear()
    }

    abstract fun onRefresh()
}
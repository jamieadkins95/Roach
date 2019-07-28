package com.jamieadkins.gwent.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

abstract class BasePresenter : MvpPresenter {

    private val disposable: CompositeDisposable = CompositeDisposable()
    val refreshRequests = BehaviorSubject.createDefault(Any())

    override fun onRefresh() {
        refreshRequests.onNext(Any())
    }

    override fun onDetach() {
        disposable.clear()
    }

    fun Disposable.addToComposite(): Disposable {
        disposable.add(this)
        return this
    }
}
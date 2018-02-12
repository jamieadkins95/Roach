package com.jamieadkins.gwent.base


import io.reactivex.observers.DisposableCompletableObserver
import timber.log.Timber

abstract class BaseDisposableCompletableObserver : DisposableCompletableObserver() {

    override fun onError(e: Throwable) {
        Timber.e(e)
    }

    override fun onComplete() {
        Timber.d("OnComplete")
    }
}

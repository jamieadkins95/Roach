package com.jamieadkins.gwent.base

import io.reactivex.observers.DisposableSingleObserver

abstract class BaseDisposableSingle<T> : DisposableSingleObserver<T>() {

    override fun onError(e: Throwable) {
        // Do nothing.
    }
}
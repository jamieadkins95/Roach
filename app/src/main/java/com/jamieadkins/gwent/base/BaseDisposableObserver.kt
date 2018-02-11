package com.jamieadkins.gwent.base

import android.util.Log
import com.crashlytics.android.Crashlytics

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

abstract class BaseDisposableObserver<T> : DisposableObserver<T>() {

    override fun onError(e: Throwable) {
        Crashlytics.logException(e)
        Log.e(javaClass.simpleName, "onError", e)
    }

    override fun onComplete() {
        Log.e(javaClass.simpleName, "onComplete")
    }
}

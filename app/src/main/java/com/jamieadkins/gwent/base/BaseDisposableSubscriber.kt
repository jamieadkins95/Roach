package com.jamieadkins.gwent.base

import android.util.Log
import com.crashlytics.android.Crashlytics
import io.reactivex.subscribers.DisposableSubscriber

abstract class BaseDisposableSubscriber<T> : DisposableSubscriber<T>() {

    override fun onError(e: Throwable) {
        Crashlytics.logException(e)
    }

    override fun onComplete() {
        Log.e(javaClass.simpleName, "onComplete")
    }
}
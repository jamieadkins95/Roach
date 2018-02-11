package com.jamieadkins.gwent.base

import android.util.Log
import com.crashlytics.android.Crashlytics
import io.reactivex.observers.DisposableSingleObserver

abstract class BaseDisposableSingle<T> : DisposableSingleObserver<T>() {

    override fun onError(e: Throwable) {
        Crashlytics.logException(e)
        Log.e(javaClass.simpleName, "onError", e)
    }
}
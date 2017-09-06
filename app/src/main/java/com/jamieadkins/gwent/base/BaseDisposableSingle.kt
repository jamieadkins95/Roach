package com.jamieadkins.gwent.base

import android.util.Log
import io.reactivex.observers.DisposableSingleObserver

abstract class BaseDisposableSingle<T> : DisposableSingleObserver<T>() {

    override fun onError(e: Throwable) {
        Log.e(javaClass.simpleName, "onError", e)
    }
}
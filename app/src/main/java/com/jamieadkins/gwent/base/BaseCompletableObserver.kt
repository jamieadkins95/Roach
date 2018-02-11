package com.jamieadkins.gwent.base

import android.util.Log

import com.jamieadkins.gwent.BuildConfig

import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver

abstract class BaseCompletableObserver : DisposableCompletableObserver() {

    override fun onError(e: Throwable) {
        Log.e(javaClass.simpleName, "Observer Error", e)
    }
}

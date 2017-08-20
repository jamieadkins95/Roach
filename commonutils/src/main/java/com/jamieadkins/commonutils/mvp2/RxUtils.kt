package com.jamieadkins.commonutils.mvp2

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun Disposable.addToComposite(composite: CompositeDisposable) { composite.add(this) }

fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.applySchedulers(): Single<T> {
    return subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}

fun Completable.applySchedulers(): Completable {
    return subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}
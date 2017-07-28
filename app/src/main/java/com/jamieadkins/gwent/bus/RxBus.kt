package com.jamieadkins.gwent.bus

import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * https://gist.github.com/jaredsburrows/e9706bd8c7d587ea0c1114a0d7651d13
 */
object RxBus {
    private val publisher = PublishSubject.create<Any>()

    fun post(event: Any) {
        publisher.onNext(event)
    }

    fun <T> register(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)

    fun <T> register(eventType: Class<T>, transformer: LifecycleTransformer<T>): Observable<T> =
            publisher.ofType(eventType)
                    .compose(transformer)
}
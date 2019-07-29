package com.jamieadkins.gwent.decktracker.bus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * https://gist.github.com/jaredsburrows/e9706bd8c7d587ea0c1114a0d7651d13
 */
object DeckTrackerEvents {
    private val publisher = PublishSubject.create<DeckTrackerEvent>()

    fun post(event: DeckTrackerEvent) {
        publisher.onNext(event)
    }

    fun <T : DeckTrackerEvent> register(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}
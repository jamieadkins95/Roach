package com.jamieadkins.gwent.deck.builder

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object DeckBuilderEvents {
    private val publisher = PublishSubject.create<DeckBuilderEvent>()

    fun post(event: DeckBuilderEvent) { publisher.onNext(event) }

    fun register(): Observable<DeckBuilderEvent> = publisher
}
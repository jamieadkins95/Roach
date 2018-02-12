package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.card.CardFilter
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

object FilterProvider {

    private val latestFilter: BehaviorSubject<CardFilter> = BehaviorSubject.create()

    init {
        latestFilter.onNext(CardFilter())
    }

    fun updateFilter(filter: CardFilter) {
        latestFilter.onNext(filter)
    }

    fun getCardFilter(): Observable<CardFilter> = latestFilter
}
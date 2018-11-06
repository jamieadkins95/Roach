package com.jamieadkins.gwent.data.filter.repository

import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.card.model.SortedBy
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor() : FilterRepository {

    private val filters = mutableMapOf<String, BehaviorSubject<CardFilter>>()

    override fun getFilter(deckId: String): Observable<CardFilter> {
        return internalGetFilter(deckId)
    }

    override fun setFilter(deckId: String, cardFilter: CardFilter) {
        internalGetFilter(deckId).onNext(cardFilter)
    }

    override fun resetFilter(deckId: String) {
        internalGetFilter(deckId).onNext(CardFilter())
    }

    private fun getKey(deckId: String): String {
        return if (deckId.isEmpty()) "card-database" else deckId
    }

    private fun internalGetFilter(deckId: String): BehaviorSubject<CardFilter> {
        return filters.getOrPut(getKey(deckId)) { BehaviorSubject.createDefault(CardFilter()) }
    }
}
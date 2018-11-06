package com.jamieadkins.gwent.domain.filter.repository

import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.card.model.SortedBy
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.GwentFaction
import io.reactivex.Observable

interface FilterRepository {

    fun getFilter(deckId: String): Observable<CardFilter>

    fun setFilter(deckId: String, cardFilter: CardFilter)

    fun resetFilter(deckId: String)
}
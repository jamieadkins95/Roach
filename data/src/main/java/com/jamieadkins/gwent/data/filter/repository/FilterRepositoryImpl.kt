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

    private val filter = BehaviorSubject.createDefault(
        CardFilter(
            GwentCardRarity.values().map { it to true }.toMap().toMutableMap(),
            GwentCardColour.values().map { it to true }.toMap().toMutableMap(),
            GwentFaction.values().map { it to true }.toMap().toMutableMap(),
            false,
            SortedBy.ALPHABETICALLY_ASC)
    )

    override fun getFilter(): Observable<CardFilter> = filter
}
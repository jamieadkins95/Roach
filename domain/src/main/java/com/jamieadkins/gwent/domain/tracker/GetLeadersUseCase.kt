package com.jamieadkins.gwent.domain.tracker

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.SortedBy
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetLeadersUseCase @Inject constructor(
    private val cardRepository: CardRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun getLeaders(faction: GwentFaction): Observable<List<GwentCard>> {
        return cardRepository.getLeaders(faction)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
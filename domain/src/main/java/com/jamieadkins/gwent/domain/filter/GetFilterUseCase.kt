package com.jamieadkins.gwent.domain.filter

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.SortedBy
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetFilterUseCase @Inject constructor(
    private val filterRepository: FilterRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun getFilter(): Observable<CardFilter> {
        return filterRepository.getFilter()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }

}
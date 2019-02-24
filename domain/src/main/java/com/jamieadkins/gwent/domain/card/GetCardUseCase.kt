package com.jamieadkins.gwent.domain.card

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.SortedBy
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetCardUseCase @Inject constructor(
    private val cardRepository: CardRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun getCard(cardId: String): Observable<GwentCard> {
        return cardRepository.getCard(cardId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
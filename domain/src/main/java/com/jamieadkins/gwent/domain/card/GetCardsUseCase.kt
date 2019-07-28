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

class GetCardsUseCase @Inject constructor(
    private val cardRepository: CardRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun getCards(): Observable<List<GwentCard>> {
        return internalGetCards(cardRepository.getCards())
    }

    fun getCards(ids: List<String>): Observable<List<GwentCard>> {
        return internalGetCards(cardRepository.getCards(ids))
    }

    fun searchCards(query: String): Observable<List<GwentCard>> {
        return internalGetCards(cardRepository.searchCards(query))
    }

    fun quickSearchCards(query: String): Observable<List<GwentCard>> {
        return internalGetCards(cardRepository.searchCards(query, true))
    }

    private fun internalGetCards(cardObservable: Observable<List<GwentCard>>): Observable<List<GwentCard>> {
        return cardObservable
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
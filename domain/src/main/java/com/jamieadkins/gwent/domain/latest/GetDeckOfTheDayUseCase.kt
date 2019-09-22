package com.jamieadkins.gwent.domain.latest

import com.jamieadkins.gwent.domain.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

class GetDeckOfTheDayUseCase @Inject constructor(
    private val deckRepository: DeckOfTheDayRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun get(): Observable<DeckOfTheDay> {
        return deckRepository.getRandomDeckOfTheDay()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
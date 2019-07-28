package com.jamieadkins.gwent.domain.tracker

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.tracker.predictions.CardPredictions
import io.reactivex.Observable
import javax.inject.Inject

class GetCardPredictionsUseCase @Inject constructor(
    private val deckTrackerRepository: DeckTrackerRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun observe(): Observable<CardPredictions> {
        return deckTrackerRepository.observePredictions()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
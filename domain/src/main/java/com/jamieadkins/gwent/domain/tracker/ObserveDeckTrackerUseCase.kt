package com.jamieadkins.gwent.domain.tracker

import com.jamieadkins.gwent.domain.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

class ObserveDeckTrackerUseCase @Inject constructor(
    private val deckTrackerRepository: DeckTrackerRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun observe(): Observable<DeckTrackerAnalysis> {
        return deckTrackerRepository.observeGame()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
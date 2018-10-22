package com.jamieadkins.gwent.domain.deck

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetDecksUseCase @Inject constructor(
    private val deckRepository: DeckRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun get(): Observable<List<GwentDeck>> {
        return deckRepository.getDecks()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
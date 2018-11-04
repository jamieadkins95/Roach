package com.jamieadkins.gwent.domain.deck

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetDeckUseCase @Inject constructor(
    private val deckRepository: DeckRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun get(deckId: String): Observable<GwentDeck> {
        return deckRepository.getDeck(deckId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
package com.jamieadkins.gwent.domain.deck

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import io.reactivex.Completable
import javax.inject.Inject

class DeleteDeckUseCase @Inject constructor(
    private val deckRepository: DeckRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun delete(deckId: String): Completable {
        return deckRepository.deleteDeck(deckId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
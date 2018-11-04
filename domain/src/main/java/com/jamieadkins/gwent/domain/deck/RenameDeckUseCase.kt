package com.jamieadkins.gwent.domain.deck

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import io.reactivex.Completable
import javax.inject.Inject

class RenameDeckUseCase @Inject constructor(
    private val deckRepository: DeckRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun rename(deckId: String, name: String): Completable {
        return deckRepository.renameDeck(deckId, name)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
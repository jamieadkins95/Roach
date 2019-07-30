package com.jamieadkins.gwent.domain.tracker

import io.reactivex.Completable
import javax.inject.Inject

class RemoveCardFromDeckTrackerUseCase @Inject constructor(
    private val deckTrackerRepository: DeckTrackerRepository
) {

    fun remove(cardId: String): Completable = deckTrackerRepository.removeOpponentCard(cardId)
}
package com.jamieadkins.gwent.domain.tracker

import javax.inject.Inject

class RemoveCardFromDeckTrackerUseCase @Inject constructor(
    private val deckTrackerRepository: DeckTrackerRepository
) {

    fun remove(cardId: String) = deckTrackerRepository.removeOpponentCard(cardId)
}
package com.jamieadkins.gwent.domain.tracker

import javax.inject.Inject

class AddCardToDeckTrackerUseCase @Inject constructor(
    private val deckTrackerRepository: DeckTrackerRepository
) {

    fun addCard(cardId: String) = deckTrackerRepository.trackOpponentCard(cardId)
}
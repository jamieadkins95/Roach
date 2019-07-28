package com.jamieadkins.gwent.domain.tracker

import com.jamieadkins.gwent.domain.GwentFaction
import javax.inject.Inject

class StartDeckTrackerUseCase @Inject constructor(
    private val deckTrackerRepository: DeckTrackerRepository
) {

    fun newGame(opponentFaction: GwentFaction, leaderId: String) = deckTrackerRepository.newGame(opponentFaction, leaderId)
}
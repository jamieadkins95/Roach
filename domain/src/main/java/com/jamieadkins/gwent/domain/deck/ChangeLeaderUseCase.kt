package com.jamieadkins.gwent.domain.deck

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import io.reactivex.Completable
import javax.inject.Inject

class ChangeLeaderUseCase @Inject constructor(
    private val deckRepository: DeckRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun change(deckId: String, leaderId: String): Completable {
        return deckRepository.setLeader(deckId, leaderId)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
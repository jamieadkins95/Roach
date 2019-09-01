package com.jamieadkins.gwent.domain.deck

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GetTryNowDeckUseCase @Inject constructor(
    private val deckRepository: DeckRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun get(): Single<String> {
        return deckRepository.getDecks().first(emptyList()).filter { it.isNotEmpty() }.map { it.first().id }
            .switchIfEmpty(deckRepository.createNewDeck("New Eredin Deck", GwentFaction.MONSTER))
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
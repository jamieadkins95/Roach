package com.jamieadkins.gwent.domain.deck

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class CreateDeckUseCase @Inject constructor(
    private val deckRepository: DeckRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun create(name: String, faction: GwentFaction): Single<String> {
        return deckRepository.createNewDeck(name, faction)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
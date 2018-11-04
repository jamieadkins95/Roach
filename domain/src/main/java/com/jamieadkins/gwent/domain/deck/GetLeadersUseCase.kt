package com.jamieadkins.gwent.domain.deck

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetLeadersUseCase @Inject constructor(
    private val cardRepository: CardRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun get(faction: GwentFaction): Observable<List<GwentCard>> {
        return cardRepository.getLeaders(faction)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
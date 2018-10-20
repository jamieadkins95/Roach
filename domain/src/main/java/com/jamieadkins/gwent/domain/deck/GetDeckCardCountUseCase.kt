package com.jamieadkins.gwent.domain.deck

import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetDeckCardCountUseCase @Inject constructor(
    private val cardRepository: CardRepository,
    private val deckRepository: DeckRepository
) {

    fun getCardCount(deckId: String): Observable<Int> {
        return deckRepository.getDeck(deckId)
            .map { deck ->
                var sum = 0
                deck.cards.values.forEach { sum += it }
                sum
            }
    }
}
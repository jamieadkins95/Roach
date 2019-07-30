package com.jamieadkins.gwent.domain.tracker

import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.deck.DeckConstants.BRONZE_MAX
import com.jamieadkins.gwent.domain.deck.DeckConstants.GOLD_MAX
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class AddCardToDeckTrackerUseCase @Inject constructor(
    private val deckTrackerRepository: DeckTrackerRepository,
    private val cardRepository: CardRepository
) {

    fun addCard(cardId: String): Completable {
        return Single.zip(
            deckTrackerRepository.getCardsPlayed().first(emptyList()),
            cardRepository.getCard(cardId).firstOrError(),
            BiFunction { cards: List<GwentCard>, newCard: GwentCard ->
                val ids = cards.map { it.id }
                when (newCard.colour) {
                    GwentCardColour.GOLD -> ids.count { it == newCard.id } < BRONZE_MAX
                    GwentCardColour.BRONZE -> ids.count { it == newCard.id } < GOLD_MAX
                    else -> false
                }
            }
        )
            .flatMapCompletable { canAdd ->
                if (canAdd) deckTrackerRepository.trackOpponentCard(cardId) else Completable.complete()
            }
    }
}
package com.jamieadkins.gwent.data.repository.deck

import com.jamieadkins.gwent.data.repository.card.GwentCardMapper
import com.jamieadkins.gwent.database.entity.DeckEntity
import com.jamieadkins.gwent.domain.deck.model.GwentDeck

object DeckMapper {
    fun deckEntityListToGwentDeckList(entityList: Collection<DeckEntity>): Collection<GwentDeck> {
        val deckList = mutableListOf<GwentDeck>()
        entityList.forEach {
            deckList.add(deckEntityToGwentDeck(it))
        }
        return deckList
    }

    fun deckEntityToGwentDeck(deckEntity: DeckEntity): GwentDeck {
        val deck = GwentDeck(deckEntity.id.toString(),
                deckEntity.name,
                GwentCardMapper.factionIdToFaction(deckEntity.factionId),
                deckEntity.leaderId)
        return deck
    }
}
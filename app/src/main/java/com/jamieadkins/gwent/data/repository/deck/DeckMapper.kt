package com.jamieadkins.gwent.data.repository.deck

import com.jamieadkins.gwent.data.Mapper
import com.jamieadkins.gwent.database.entity.DeckEntity
import com.jamieadkins.gwent.model.GwentDeck

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
                Mapper.factionIdToFaction(deckEntity.factionId),
                deckEntity.leaderId)
        return deck
    }
}
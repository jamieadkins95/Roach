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
        val deck = GwentDeck()
        deck.id = deckEntity.id.toString()
        deck.name = deckEntity.name
        deck.leaderId = deckEntity.leaderId
        deck.cards = deckEntity.cards

        deck.faction = Mapper.factionIdToFaction(deckEntity.factionId)
        return deck
    }
}
package com.jamieadkins.gwent.database.entity

import androidx.room.Embedded
import androidx.room.Relation

class DeckWithCardsEntity {
    @Embedded
    lateinit var deck: DeckEntity

    @Relation(parentColumn = "id", entityColumn = "deckId")
    lateinit var cards: List<DeckCardEntity>
}
package com.jamieadkins.gwent.database.entity

import androidx.room.Embedded
import androidx.room.Relation

class DeckTrackerGameWithCardsEntity {
    @Embedded
    lateinit var deck: DeckTrackerGameEntity

    @Relation(parentColumn = "id", entityColumn = "gameId")
    lateinit var cards: List<DeckTrackerCardEntity>
}
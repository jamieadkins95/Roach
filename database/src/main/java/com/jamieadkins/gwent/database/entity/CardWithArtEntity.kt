package com.jamieadkins.gwent.database.entity

import androidx.room.Embedded
import androidx.room.Relation

class CardWithArtEntity {
    @Embedded
    lateinit var card: CardEntity

    @Relation(parentColumn = "id", entityColumn = "cardId")
    lateinit var art: List<ArtEntity>
}
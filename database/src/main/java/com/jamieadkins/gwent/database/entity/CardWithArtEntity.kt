package com.jamieadkins.gwent.database.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class CardWithArtEntity {
    @Embedded
    lateinit var card: CardEntity

    @Relation(parentColumn = "id", entityColumn = "cardId")
    lateinit var art: List<ArtEntity>
}
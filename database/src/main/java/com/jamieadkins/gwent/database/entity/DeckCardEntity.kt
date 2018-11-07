package com.jamieadkins.gwent.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jamieadkins.gwent.database.GwentDatabase

@Entity(foreignKeys = [
    (ForeignKey(entity = CardEntity::class,
                onDelete = ForeignKey.NO_ACTION,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("cardId"))),
    (ForeignKey(entity = DeckEntity::class,
                onDelete = ForeignKey.CASCADE,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("deckId")))],
        tableName = GwentDatabase.DECK_CARD_TABLE,
        indices = [Index(value = ["cardId"]), Index(value = ["deckId"])],
        primaryKeys = ["deckId", "cardId"])
data class DeckCardEntity(
    val deckId: String,
    val cardId: String,
    val count: Int = 0)
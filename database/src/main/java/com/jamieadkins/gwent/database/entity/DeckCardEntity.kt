package com.jamieadkins.gwent.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import com.jamieadkins.gwent.database.GwentDatabase

@Entity(foreignKeys = [
    (ForeignKey(entity = CardEntity::class,
                onDelete = ForeignKey.CASCADE,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("cardId"))),
    (ForeignKey(entity = DeckEntity::class,
                onDelete = ForeignKey.CASCADE,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("deckId")))],
        tableName = GwentDatabase.DECK_CARD_TABLE,
        indices = [Index(value = ["cardId"]), Index(value = ["deckId"])])
data class DeckCardEntity(
    val deckId: String,
    val cardId: String,
    val count: Int = 0) {

    @PrimaryKey(autoGenerate = true) var id: Long? = null
}
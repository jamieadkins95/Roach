package com.jamieadkins.gwent.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jamieadkins.gwent.database.GwentDatabase

@Entity(foreignKeys = [
    ForeignKey(entity = CardEntity::class,
                onDelete = ForeignKey.NO_ACTION,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("cardId")),
    ForeignKey(entity = DeckTrackerGameEntity::class,
               onDelete = ForeignKey.CASCADE,
               parentColumns = arrayOf("id"),
               childColumns = arrayOf("gameId"))],
        tableName = GwentDatabase.DECK_TRACKER_CARDS_TABLE,
        indices = [Index(value = ["cardId"]), Index(value = ["gameId"])],
        primaryKeys = ["gameId", "cardId"])
data class DeckTrackerCardEntity(
    val gameId: String,
    val cardId: String,
    val count: Int = 0,
    val opponentPlayed: Boolean = false)
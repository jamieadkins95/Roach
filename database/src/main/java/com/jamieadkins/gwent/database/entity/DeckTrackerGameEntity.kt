package com.jamieadkins.gwent.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.jamieadkins.gwent.database.GwentDatabase
import java.util.*

@Entity(
    foreignKeys = [
        (ForeignKey(
            entity = DeckEntity::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("deckId")))],
    tableName = GwentDatabase.DECK_TRACKER_GAME_TABLE)
data class DeckTrackerGameEntity(
    val deckId: String,
    val factionId: String,
    val enemyFactionId: String,
    val enemyLeaderId: String,
    val win: Boolean = false,
    val completed: Boolean = false,
    val deleted: Boolean = false,
    val started: Date = Date()) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
package com.jamieadkins.gwent.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jamieadkins.gwent.database.GwentDatabase
import java.util.*

@Entity(tableName = GwentDatabase.DECK_TABLE)
data class DeckEntity(
    val name: String,
    val factionId: String,
    val leaderId: String,
    val deleted: Boolean = false,
    val created: Date = Date()) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
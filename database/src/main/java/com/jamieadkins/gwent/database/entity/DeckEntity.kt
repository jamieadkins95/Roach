package com.jamieadkins.gwent.database.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.jamieadkins.gwent.database.GwentDatabase

@Entity(tableName = GwentDatabase.DECK_TABLE)
data class DeckEntity(
        val name: String,
        val factionId: String,
        val leaderId: String? = null,
        val deleted: Boolean = false) {

    @PrimaryKey(autoGenerate = true) var id: Long? = null
}
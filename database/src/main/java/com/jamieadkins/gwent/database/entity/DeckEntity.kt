package com.jamieadkins.gwent.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.jamieadkins.gwent.database.GwentDatabase

@Entity(tableName = GwentDatabase.DECK_TABLE)
data class DeckEntity(
        val name: String,
        val factionId: String,
        val leaderId: String? = null,
        val deleted: Boolean = false) {

    @PrimaryKey(autoGenerate = true) val id: Long? = null
}
package com.jamieadkins.gwent.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jamieadkins.gwent.database.GwentDatabase

@Entity(foreignKeys = [
    ForeignKey(entity = CardEntity::class,
               onDelete = ForeignKey.CASCADE,
               parentColumns = arrayOf("id"),
               childColumns = arrayOf("cardId"))],
        tableName = GwentDatabase.ART_TABLE,
        indices = [Index(value = ["cardId"])])
data class ArtEntity(
    @PrimaryKey val artId: String,
    val cardId: String,
    val original: String? = null,
    val high: String? = null,
    val medium: String? = null,
    val low: String? = null,
    val thumbnail: String? = null)
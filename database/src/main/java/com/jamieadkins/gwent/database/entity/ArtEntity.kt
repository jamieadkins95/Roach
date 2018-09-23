package com.jamieadkins.gwent.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
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
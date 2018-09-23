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
                childColumns = arrayOf("cardId")))],
        tableName = GwentDatabase.COLLECTION_TABLE,
        indices = [Index(value = ["cardId"])])
data class CollectionEntity(
    val cardId: String,
    val variationId: String = "${cardId}00",
    val count: Int = 0) {

    @PrimaryKey(autoGenerate = true) var id: Long? = null
}
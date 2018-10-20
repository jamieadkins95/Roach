package com.jamieadkins.gwent.database.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.jamieadkins.gwent.database.GwentDatabase

@Entity(tableName = GwentDatabase.KEYWORD_TABLE)
data class KeywordEntity(
        val keywordId: String,
        val locale: String,
        val name: String,
        val description: String) {

    @PrimaryKey(autoGenerate = true) var id: Long? = null
}
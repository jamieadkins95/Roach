package com.jamieadkins.gwent.database.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.jamieadkins.gwent.database.GwentDatabase

@Entity(tableName = GwentDatabase.CATEGORY_TABLE)
data class CategoryEntity(
        val categoryId: String,
        val locale: String,
        val name: String) {

    @PrimaryKey(autoGenerate = true) var id: Long? = null
}
package com.jamieadkins.gwent.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.database.ListConverters
import com.jamieadkins.gwent.database.MapConverters

@Entity(tableName = GwentDatabase.CARD_TABLE)
data class CardEntity(
        @PrimaryKey val id: String,
        val strength: Int = 0,
        val collectible: Boolean? = null,
        val rarity: String? = null,
        val color: String? = null,
        val faction: String? = null,
        @TypeConverters(MapConverters::class) val name: Map<String, String>? = null,
        @TypeConverters(MapConverters::class) val tooltip: Map<String, String>? = null,
        @TypeConverters(MapConverters::class) val flavor: Map<String, String>? = null,
        @TypeConverters(ListConverters::class) val categoryIds: List<String>? = null,
        @TypeConverters(ListConverters::class) val loyalties: List<String>? = null,
        @TypeConverters(ListConverters::class) val related: List<String>? = null)
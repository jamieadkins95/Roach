package com.jamieadkins.gwent.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.jamieadkins.gwent.database.entity.CardEntity

@Database(entities = [CardEntity::class], version = 1)
abstract class GwentDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "GwentDatabase"
        const val CARD_TABLE = "cards"
        const val ART_TABLE = "art"
    }

    abstract fun cardDao(): CardDao
}

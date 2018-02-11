package com.jamieadkins.gwent.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.jamieadkins.gwent.database.entity.ArtEntity
import com.jamieadkins.gwent.database.entity.CardEntity
import com.jamieadkins.gwent.database.entity.PatchVersionEntity

@Database(entities = [CardEntity::class, ArtEntity::class, PatchVersionEntity::class], version = 1)
@TypeConverters(DatabaseConverters::class)
abstract class GwentDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "GwentDatabase"
        const val CARD_TABLE = "cards"
        const val ART_TABLE = "art"
        const val PATCH_VERSION_TABLE = "patch"
    }

    abstract fun cardDao(): CardDao

    abstract fun patchDao(): PatchDao
}

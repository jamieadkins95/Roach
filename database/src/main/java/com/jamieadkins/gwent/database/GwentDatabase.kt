package com.jamieadkins.gwent.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.jamieadkins.gwent.database.entity.*

@Database(entities = [CardEntity::class, ArtEntity::class, PatchVersionEntity::class,
    DeckEntity::class, DeckCardEntity::class], version = 1)
@TypeConverters(DatabaseConverters::class)
abstract class GwentDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "GwentDatabase"
        const val CARD_TABLE = "cards"
        const val ART_TABLE = "art"
        const val PATCH_VERSION_TABLE = "patch"
        const val DECK_TABLE = "decks"
        const val DECK_CARD_TABLE = "deck_cards"
    }

    abstract fun cardDao(): CardDao

    abstract fun patchDao(): PatchDao

    abstract fun deckDao(): DeckDao

    abstract fun deckCardDao(): DeckCardDao
}

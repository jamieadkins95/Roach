package com.jamieadkins.gwent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jamieadkins.gwent.database.GwentDatabase.Companion.DATABASE_VERSION
import com.jamieadkins.gwent.database.entity.*

@Database(entities = [CardEntity::class, ArtEntity::class,
    DeckEntity::class, DeckCardEntity::class, CollectionEntity::class, CategoryEntity::class,
    KeywordEntity::class], version = DATABASE_VERSION)
@TypeConverters(DatabaseConverters::class)
abstract class GwentDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "GwentDatabase"
        const val CARD_TABLE = "cards"
        const val ART_TABLE = "art"
        const val DECK_TABLE = "decks"
        const val DECK_CARD_TABLE = "deck_cards"
        const val COLLECTION_TABLE = "collection"
        const val CATEGORY_TABLE = "category"
        const val KEYWORD_TABLE = "keyword"

        const val DATABASE_VERSION = 5
    }

    abstract fun cardDao(): CardDao

    abstract fun artDao(): ArtDao

    abstract fun deckDao(): DeckDao

    abstract fun deckCardDao(): DeckCardDao

    abstract fun collectionDao(): CollectionDao

    abstract fun keywordDao(): KeywordDao

    abstract fun categoryDao(): CategoryDao
}

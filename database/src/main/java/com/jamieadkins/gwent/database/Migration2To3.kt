package com.jamieadkins.gwent.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration2To3 : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE ${GwentDatabase.DECK_TABLE} " + " ADD COLUMN created INTEGER NOT NULL DEFAULT 0")
    }
}
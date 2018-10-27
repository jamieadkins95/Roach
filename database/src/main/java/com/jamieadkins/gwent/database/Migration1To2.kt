package com.jamieadkins.gwent.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration1To2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE ${GwentDatabase.CARD_TABLE} " + " ADD COLUMN type TEXT NOT NULL DEFAULT ''")
    }
}
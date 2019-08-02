package com.jamieadkins.gwent.base

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jamieadkins.gwent.database.GwentDatabase
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun database(context: Context, migrations: Set<@JvmSuppressWildcards Migration>): GwentDatabase {
        return Room.databaseBuilder(context, GwentDatabase::class.java, GwentDatabase.DB_NAME)
            .addMigrations(*migrations.toTypedArray())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @IntoSet
    fun migration5_6(): Migration = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${GwentDatabase.CARD_TABLE} ADD COLUMN secondaryFaction TEXT DEFAULT NULL")
        }
    }

    @Provides
    @IntoSet
    fun migration6_7(): Migration = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${GwentDatabase.CARD_TABLE} ADD COLUMN expansion TEXT NOT NULL DEFAULT 'BaseSet'")
        }
    }
}
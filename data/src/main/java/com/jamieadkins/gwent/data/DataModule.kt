package com.jamieadkins.gwent.data

import androidx.room.Room
import android.content.Context
import android.preference.PreferenceManager
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.database.Migration1To2
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun preferences(context: Context): RxSharedPreferences {
        return RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(context))
    }

    @Provides
    @Named("files")
    fun filesDir(context: Context): File = context.filesDir

    @Provides
    @Named("cache")
    fun cacheDir(context: Context): File = context.cacheDir

    @Provides
    @Singleton
    fun database(context: Context): GwentDatabase {
        return Room.databaseBuilder(context, GwentDatabase::class.java, GwentDatabase.DB_NAME)
            .addMigrations(Migration1To2)
            .build()
    }
}
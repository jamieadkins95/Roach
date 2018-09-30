package com.jamieadkins.gwent.data

import android.arch.persistence.room.Room
import android.content.Context
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jamieadkins.gwent.database.GwentDatabase
import dagger.Module
import dagger.Provides
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
    fun database(context: Context): GwentDatabase = Room.databaseBuilder(context, GwentDatabase::class.java, GwentDatabase.DB_NAME).build()
}
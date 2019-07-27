package com.jamieadkins.gwent.base

import android.content.Context
import androidx.room.Room
import com.jamieadkins.gwent.database.GwentDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun database(context: Context): GwentDatabase {
        return Room.databaseBuilder(context, GwentDatabase::class.java, GwentDatabase.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}
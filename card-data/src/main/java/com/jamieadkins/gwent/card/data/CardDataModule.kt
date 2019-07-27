package com.jamieadkins.gwent.card.data

import android.content.Context
import androidx.room.Room
import com.jamieadkins.gwent.database.GwentDatabase
import com.jamieadkins.gwent.domain.LocaleRepository
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class CardDataModule {

    @Binds
    @Singleton
    abstract fun repository(repository: CardRepositoryImpl): CardRepository

    @Binds
    @Singleton
    abstract fun locale(repository: LocaleRepositoryImpl): LocaleRepository

    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun database(context: Context): GwentDatabase {
            return Room.databaseBuilder(context, GwentDatabase::class.java, GwentDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
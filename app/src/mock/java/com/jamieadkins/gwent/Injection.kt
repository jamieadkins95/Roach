package com.jamieadkins.gwent


import android.support.v7.preference.PreferenceManager
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.SchedulerProvider
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.card.CardRepositoryImpl
import com.jamieadkins.gwent.data.repository.deck.DeckRepository
import com.jamieadkins.gwent.data.repository.deck.UserDeckRepository
import com.jamieadkins.gwent.data.repository.filter.FilterRepository
import com.jamieadkins.gwent.data.repository.filter.FilterRepositoryImpl
import com.jamieadkins.gwent.data.repository.update.UpdateRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepositoryImpl
import com.jamieadkins.gwent.database.GwentDatabaseProvider
import com.jamieadkins.gwent.main.GwentApplication

object Injection {

    private val database by lazy { GwentDatabaseProvider.getDatabase(GwentApplication.INSTANCE.applicationContext) }
    private val filterRepositories = mutableMapOf<String, FilterRepository>()

    fun provideDeckRepository(): DeckRepository {
        return UserDeckRepository(database)
    }

    fun provideCardRepository(): CardRepository {
        return CardRepositoryImpl(database)
    }

    fun provideUpdateRepository(): UpdateRepository {
        return UpdateRepositoryImpl(database,
                PreferenceManager.getDefaultSharedPreferences(GwentApplication.INSTANCE.applicationContext),
                GwentApplication.INSTANCE.applicationContext.resources)
    }

    fun provideFilterRepository(key: String): FilterRepository {
        return filterRepositories.getOrPut(key, { FilterRepositoryImpl() })
    }

    fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider
    }
}
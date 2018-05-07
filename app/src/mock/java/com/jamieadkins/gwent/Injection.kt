package com.jamieadkins.gwent


import android.support.v7.preference.PreferenceManager
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.SchedulerProvider
import com.jamieadkins.gwent.data.StoreManager
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.data.card.repository.CardRepositoryImpl
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import com.jamieadkins.gwent.data.deck.repository.UserDeckRepository
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import com.jamieadkins.gwent.data.filter.repository.FilterRepositoryImpl
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import com.jamieadkins.gwent.data.update.repository.UpdateRepositoryImpl
import com.jamieadkins.gwent.database.GwentDatabaseProvider
import com.jamieadkins.gwent.main.GwentApplication

object Injection {

    private val database by lazy { GwentDatabaseProvider.getDatabase(GwentApplication.INSTANCE.applicationContext) }
    private val storeManager by lazy { StoreManager(GwentApplication.INSTANCE.cacheDir) }
    private val filterRepositories = mutableMapOf<String, FilterRepository>()

    fun provideDeckRepository(): DeckRepository {
        return UserDeckRepository(database)
    }

    fun provideCardRepository(): CardRepository {
        return CardRepositoryImpl(database)
    }

    fun provideUpdateRepository(): UpdateRepository {
        return UpdateRepositoryImpl(database,
                GwentApplication.INSTANCE.filesDir,
                storeManager,
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
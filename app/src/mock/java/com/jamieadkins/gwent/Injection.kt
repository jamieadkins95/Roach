package com.jamieadkins.gwent

import android.support.v7.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.SchedulerProvider
import com.jamieadkins.gwent.data.FactionMapper
import com.jamieadkins.gwent.data.LocaleRepositoryImpl
import com.jamieadkins.gwent.data.StoreManager
import com.jamieadkins.gwent.data.card.mapper.ApiMapper
import com.jamieadkins.gwent.data.card.mapper.ArtApiMapper
import com.jamieadkins.gwent.data.card.mapper.GwentCardArtMapper
import com.jamieadkins.gwent.data.card.mapper.GwentCardMapper
import com.jamieadkins.gwent.data.card.repository.CardRepositoryImpl
import com.jamieadkins.gwent.data.deck.mapper.DeckMapper
import com.jamieadkins.gwent.data.deck.repository.UserDeckRepository
import com.jamieadkins.gwent.data.filter.repository.FilterRepositoryImpl
import com.jamieadkins.gwent.data.update.repository.CardUpdateRepository
import com.jamieadkins.gwent.data.update.repository.CategoryUpdateRepository
import com.jamieadkins.gwent.data.update.repository.KeywordUpdateRepository
import com.jamieadkins.gwent.data.update.repository.PatchRepository
import com.jamieadkins.gwent.data.update.repository.UpdateRepositoryImpl
import com.jamieadkins.gwent.database.GwentDatabaseProvider
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import com.jamieadkins.gwent.main.GwentApplication

object Injection {

    private val filesDir = GwentApplication.INSTANCE.filesDir
    private val resources by lazy { GwentApplication.INSTANCE.applicationContext.resources }
    private val database by lazy { GwentDatabaseProvider.getDatabase(GwentApplication.INSTANCE.applicationContext) }
    private val storeManager by lazy { StoreManager(GwentApplication.INSTANCE.cacheDir) }
    private val preferences by lazy { RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(GwentApplication.INSTANCE.applicationContext)) }
    private val filterRepositories = mutableMapOf<String, FilterRepository>()

    private val factionMapper by lazy { FactionMapper() }
    private val artMapper by lazy { GwentCardArtMapper() }
    private val cardMapper by lazy { GwentCardMapper(artMapper, factionMapper) }
    private val deckMapper by lazy { DeckMapper(factionMapper) }

    private val artApiMapper by lazy { ArtApiMapper() }
    private val cardApiMapper by lazy { ApiMapper() }

    private val localeRepository by lazy { LocaleRepositoryImpl(preferences, resources) }
    private val patchRepository by lazy { PatchRepository(storeManager) }
    private val cardUpdateRepository by lazy { CardUpdateRepository(database, filesDir, patchRepository, cardApiMapper, artApiMapper, preferences) }
    private val keywordUpdateRepository by lazy { KeywordUpdateRepository(database, filesDir, patchRepository, preferences) }
    private val categoryUpdateRepository by lazy { CategoryUpdateRepository(database, filesDir, patchRepository, preferences) }

    fun provideDeckRepository(): DeckRepository {
        return UserDeckRepository(database, cardMapper, factionMapper, deckMapper, localeRepository)
    }

    fun provideCardRepository(): CardRepository {
        return CardRepositoryImpl(database, cardMapper, localeRepository)
    }

    fun provideUpdateRepository(): UpdateRepository {
        return UpdateRepositoryImpl(
            database,
            filesDir,
            preferences,
            resources, cardUpdateRepository, keywordUpdateRepository, categoryUpdateRepository, patchRepository)
    }

    fun provideFilterRepository(key: String): FilterRepository {
        return filterRepositories.getOrPut(key, { FilterRepositoryImpl() })
    }

    fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider
    }
}
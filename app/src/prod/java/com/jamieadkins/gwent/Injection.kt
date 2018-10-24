package com.jamieadkins.gwent

import androidx.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.SchedulerProvider
import com.jamieadkins.gwent.data.FactionMapper
import com.jamieadkins.gwent.data.LocaleRepositoryImpl
import com.jamieadkins.gwent.data.card.mapper.GwentCardArtMapper
import com.jamieadkins.gwent.data.card.mapper.GwentCardMapper
import com.jamieadkins.gwent.data.card.repository.CardRepositoryImpl
import com.jamieadkins.gwent.data.filter.repository.FilterRepositoryImpl
import com.jamieadkins.gwent.database.GwentDatabaseProvider
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import com.jamieadkins.gwent.main.GwentApplication

object Injection {

    private val resources by lazy { GwentApplication.INSTANCE.applicationContext.resources }
    private val database by lazy { GwentDatabaseProvider.getDatabase(GwentApplication.INSTANCE.applicationContext) }
    private val preferences by lazy {
        RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(GwentApplication.INSTANCE.applicationContext))
    }
    private val filterRepositories = mutableMapOf<String, FilterRepository>()

    private val factionMapper by lazy { FactionMapper() }
    private val artMapper by lazy { GwentCardArtMapper() }
    private val cardMapper by lazy { GwentCardMapper(artMapper, factionMapper) }

    private val localeRepository by lazy { LocaleRepositoryImpl(preferences, resources) }

    fun provideCardRepository(): CardRepository {
        return CardRepositoryImpl(database, cardMapper, localeRepository)
    }

    fun provideFilterRepository(key: String): FilterRepository {
        return filterRepositories.getOrPut(key, { FilterRepositoryImpl() })
    }

    fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider
    }
}
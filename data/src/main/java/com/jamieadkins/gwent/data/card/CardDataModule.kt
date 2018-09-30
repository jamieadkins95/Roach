package com.jamieadkins.gwent.data.card

import com.jamieadkins.gwent.data.LocaleRepositoryImpl
import com.jamieadkins.gwent.data.card.repository.CardRepositoryImpl
import com.jamieadkins.gwent.data.filter.repository.FilterRepositoryImpl
import com.jamieadkins.gwent.domain.LocaleRepository
import com.jamieadkins.gwent.domain.card.CardDatabase
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class CardDataModule {

    @Binds
    @Singleton
    abstract fun repository(repository: CardRepositoryImpl): CardRepository

    @Binds
    @Singleton
    abstract fun locale(repository: LocaleRepositoryImpl): LocaleRepository

    @Binds
    @Singleton
    @CardDatabase
    abstract fun filter(repository: FilterRepositoryImpl): FilterRepository
}
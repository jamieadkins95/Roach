package com.jamieadkins.gwent.data.card

import com.jamieadkins.gwent.card.data.LocaleRepositoryImpl
import com.jamieadkins.gwent.card.data.CardRepositoryImpl
import com.jamieadkins.gwent.domain.LocaleRepository
import com.jamieadkins.gwent.domain.card.repository.CardRepository
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
}
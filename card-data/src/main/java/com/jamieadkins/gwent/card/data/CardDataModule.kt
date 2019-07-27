package com.jamieadkins.gwent.card.data

import com.jamieadkins.gwent.domain.LocaleRepository
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
abstract class CardDataModule {

    @Binds
    @Reusable
    abstract fun repository(repository: CardRepositoryImpl): CardRepository

    @Binds
    @Reusable
    abstract fun locale(repository: LocaleRepositoryImpl): LocaleRepository
}
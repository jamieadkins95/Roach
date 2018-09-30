package com.jamieadkins.gwent.data.card

import com.jamieadkins.gwent.data.card.repository.CardRepositoryImpl
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import dagger.Binds
import dagger.Module

@Module
abstract class CardDataModule {

    @Binds
    abstract fun repository(repository: CardRepositoryImpl): CardRepository
}
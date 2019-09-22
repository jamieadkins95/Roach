package com.jamieadkins.gwent.data.latest

import com.jamieadkins.gwent.domain.latest.DeckOfTheDayRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
abstract class DeckOfTheDayDataModule {

    @Binds
    @Reusable
    abstract fun repository(repository: DeckOfTheDayRepositoryImpl): DeckOfTheDayRepository

}
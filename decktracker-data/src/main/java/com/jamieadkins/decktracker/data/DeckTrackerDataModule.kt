package com.jamieadkins.decktracker.data

import com.jamieadkins.gwent.domain.tracker.DeckTrackerRepository
import dagger.Binds
import dagger.Module

@Module(includes = [CardPredictorDataModule::class])
abstract class DeckTrackerDataModule {

    @Binds
    @DeckTrackerScope
    abstract fun repository(repository: DeckTrackerRepositoryImpl): DeckTrackerRepository

}
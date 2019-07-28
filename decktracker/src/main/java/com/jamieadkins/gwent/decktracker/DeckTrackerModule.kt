package com.jamieadkins.gwent.decktracker

import com.jamieadkins.decktracker.data.DeckTrackerScope
import dagger.Binds
import dagger.Module

@Module
abstract class DeckTrackerModule {

    @Binds
    @DeckTrackerScope
    internal abstract fun view(view: DeckTrackerActivity): DeckTrackerContract.View

    @Binds
    @DeckTrackerScope
    internal abstract fun presenter(presenter: DeckTrackerPresenter): DeckTrackerContract.Presenter
}
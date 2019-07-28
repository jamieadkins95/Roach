package com.jamieadkins.gwent.tracker

import com.jamieadkins.gwent.di.FragmentScoped
import dagger.Binds
import dagger.Module

@Module
abstract class DeckTrackerSetupModule {

    @FragmentScoped
    @Binds
    internal abstract fun view(view: DeckTrackerSetupFragment): DeckTrackerSetupContract.View

    @FragmentScoped
    @Binds
    internal abstract fun presenter(presenter: DeckTrackerSetupPresenter): DeckTrackerSetupContract.Presenter
}
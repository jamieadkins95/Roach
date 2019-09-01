package com.jamieadkins.gwent.deckbuilder

import com.jamieadkins.gwent.di.FragmentScoped
import dagger.Binds
import dagger.Module

@Module
abstract class DeckDetailsModule {

    @FragmentScoped
    @Binds
    internal abstract fun view(view: DeckDetailsFragment): DeckDetailsContract.View

    @FragmentScoped
    @Binds
    internal abstract fun presenter(presenter: DeckDetailsPresenter): DeckDetailsContract.Presenter
}
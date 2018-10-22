package com.jamieadkins.gwent.deck.list

import com.jamieadkins.gwent.di.FragmentScoped
import dagger.Binds
import dagger.Module

@Module
abstract class DeckListModule {

    @FragmentScoped
    @Binds
    internal abstract fun view(view: DeckListFragment): DeckListContract.View

    @FragmentScoped
    @Binds
    internal abstract fun presenter(presenter: DeckListPresenter): DeckListContract.Presenter
}
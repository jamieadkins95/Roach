package com.jamieadkins.gwent.deck.detail.rename

import com.jamieadkins.gwent.di.FragmentScoped
import dagger.Binds
import dagger.Module

@Module
abstract class RenameDeckModule {

    @FragmentScoped
    @Binds
    internal abstract fun view(view: RenameDeckDialog): RenameDeckContract.View

    @FragmentScoped
    @Binds
    internal abstract fun presenter(presenter: RenameDeckPresenter): RenameDeckContract.Presenter
}
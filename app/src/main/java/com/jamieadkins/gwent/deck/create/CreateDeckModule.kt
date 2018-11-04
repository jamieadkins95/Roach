package com.jamieadkins.gwent.deck.create

import com.jamieadkins.gwent.di.FragmentScoped
import dagger.Binds
import dagger.Module

@Module
abstract class CreateDeckModule {

    @FragmentScoped
    @Binds
    internal abstract fun view(view: CreateDeckDialog): CreateDeckContract.View

    @FragmentScoped
    @Binds
    internal abstract fun presenter(presenter: CreateDeckPresenter): CreateDeckContract.Presenter
}
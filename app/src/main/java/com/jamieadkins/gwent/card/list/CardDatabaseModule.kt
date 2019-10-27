package com.jamieadkins.gwent.card.list

import com.jamieadkins.gwent.di.FragmentScoped
import dagger.Binds
import dagger.Module

@Module
abstract class CardDatabaseModule {

    @FragmentScoped
    @Binds
    internal abstract fun view(activity: CardDatabaseFragment): CardDatabaseContract.View

    @FragmentScoped
    @Binds
    internal abstract fun presenter(presenter: CardDatabasePresenter): CardDatabaseContract.Presenter
}
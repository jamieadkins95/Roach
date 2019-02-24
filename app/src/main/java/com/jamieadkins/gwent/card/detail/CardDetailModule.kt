package com.jamieadkins.gwent.card.detail

import com.jamieadkins.gwent.di.FragmentScoped
import dagger.Binds
import dagger.Module

@Module
abstract class CardDetailModule {

    @FragmentScoped
    @Binds
    internal abstract fun view(view: CardDetailsFragment): DetailContract.View

    @FragmentScoped
    @Binds
    internal abstract fun presenter(presenter: DetailPresenter): DetailContract.Presenter
}
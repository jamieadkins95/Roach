package com.jamieadkins.gwent.latest

import com.jamieadkins.gwent.di.FragmentScoped
import dagger.Binds
import dagger.Module

@Module
abstract class GwentLatestModule {

    @FragmentScoped
    @Binds
    internal abstract fun view(activity: GwentFragment): GwentLatestContract.View

    @FragmentScoped
    @Binds
    internal abstract fun presenter(presenter: GwentLatestPresenter): GwentLatestContract.Presenter
}
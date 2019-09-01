package com.jamieadkins.gwent.deckbuilder.trynow

import com.jamieadkins.gwent.di.ActivityScoped
import dagger.Binds
import dagger.Module

@Module
abstract class TryNowModule {

    @ActivityScoped
    @Binds
    internal abstract fun view(activity: TryNowActivity): TryNowContract.View

    @ActivityScoped
    @Binds
    internal abstract fun presenter(presenter: TryNowPresenter): TryNowContract.Presenter
}
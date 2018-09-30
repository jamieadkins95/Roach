package com.jamieadkins.gwent.update

import com.jamieadkins.gwent.di.ActivityScoped
import dagger.Binds
import dagger.Module

@Module
abstract class UpdateModule {

    @ActivityScoped
    @Binds
    internal abstract fun view(activity: UpdateActivity): UpdateContract.View

    @ActivityScoped
    @Binds
    internal abstract fun presenter(presenter: UpdatePresenter): UpdateContract.Presenter
}
package com.jamieadkins.gwent.launch

import com.jamieadkins.gwent.di.ActivityScoped
import dagger.Binds
import dagger.Module

@Module
abstract class LaunchModule {

    @ActivityScoped
    @Binds
    internal abstract fun view(activity: LaunchActivity): LaunchContract.View

    @ActivityScoped
    @Binds
    internal abstract fun presenter(presenter: LaunchPresenter): LaunchContract.Presenter
}
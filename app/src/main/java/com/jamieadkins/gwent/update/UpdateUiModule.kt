package com.jamieadkins.gwent.update

import com.jamieadkins.gwent.di.ActivityScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UpdateUiModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [UpdateModule::class])
    internal abstract fun service(): UpdateService
}
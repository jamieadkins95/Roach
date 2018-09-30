package com.jamieadkins.gwent.di

import com.jamieadkins.gwent.data.update.UpdateDataModule
import com.jamieadkins.gwent.update.UpdateActivity
import com.jamieadkins.gwent.update.UpdateModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityInjectionModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [UpdateModule::class, UpdateDataModule::class])
    internal abstract fun activity(): UpdateActivity
}
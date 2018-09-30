package com.jamieadkins.gwent.update

import com.jamieadkins.gwent.data.update.UpdateDataModule
import com.jamieadkins.gwent.di.ActivityScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UpdateModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [UpdateDataModule::class])
    internal abstract fun activity(): UpdateActivity

    @ActivityScoped
    @Binds
    internal abstract fun view(activity: UpdateActivity): UpdateContract.View

    @ActivityScoped
    @Binds
    internal abstract fun presenter(presenter: UpdatePresenter): UpdateContract.Presenter
}
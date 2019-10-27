package com.jamieadkins.gwent.deck.builder.leader

import com.jamieadkins.gwent.di.FragmentScoped
import dagger.Binds
import dagger.Module

@Module
abstract class LeaderPickerModule {

    @FragmentScoped
    @Binds
    internal abstract fun view(view: LeaderPickerDialog): LeaderPickerContract.View

    @FragmentScoped
    @Binds
    internal abstract fun presenter(presenter: LeaderPickerPresenter): LeaderPickerContract.Presenter
}
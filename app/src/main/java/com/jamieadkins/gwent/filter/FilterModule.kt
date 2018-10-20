package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.di.FragmentScoped
import dagger.Binds
import dagger.Module

@Module
abstract class FilterModule {

    @Binds
    @FragmentScoped
    internal abstract fun view(view: FilterBottomSheetDialogFragment): FilterContract.View

    @Binds
    @FragmentScoped
    internal abstract fun presenter(presenter: FilterPresenter): FilterContract.Presenter
}
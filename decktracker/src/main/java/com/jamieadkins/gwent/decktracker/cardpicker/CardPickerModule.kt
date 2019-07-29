package com.jamieadkins.gwent.decktracker.cardpicker

import dagger.Binds
import dagger.Module

@Module
abstract class CardPickerModule {

    @Binds
    internal abstract fun view(view: CardPickerDialog): CardPickerContract.View

    @Binds
    internal abstract fun presenter(presenter: CardPickerPresenter): CardPickerContract.Presenter
}
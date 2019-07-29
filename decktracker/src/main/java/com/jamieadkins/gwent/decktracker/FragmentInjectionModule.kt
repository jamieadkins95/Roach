package com.jamieadkins.gwent.decktracker

import com.jamieadkins.gwent.decktracker.cardpicker.CardPickerDialog
import com.jamieadkins.gwent.decktracker.cardpicker.CardPickerModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentInjectionModule private constructor() {

    @ContributesAndroidInjector(modules = [CardPickerModule::class])
    internal abstract fun cardPicker(): CardPickerDialog
}
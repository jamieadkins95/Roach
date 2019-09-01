package com.jamieadkins.gwent.deckbuilder.di

import com.jamieadkins.gwent.deckbuilder.DeckDetailsFragment
import com.jamieadkins.gwent.deckbuilder.DeckDetailsModule
import com.jamieadkins.gwent.deckbuilder.leader.LeaderPickerDialog
import com.jamieadkins.gwent.deckbuilder.leader.LeaderPickerModule
import com.jamieadkins.gwent.deckbuilder.rename.RenameDeckDialog
import com.jamieadkins.gwent.deckbuilder.rename.RenameDeckModule
import com.jamieadkins.gwent.di.FragmentScoped
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment
import com.jamieadkins.gwent.filter.FilterModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DeckBuilderFragmentInjectionModule private constructor() {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [DeckDetailsModule::class])
    internal abstract fun deckBuilder(): DeckDetailsFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [LeaderPickerModule::class])
    internal abstract fun leaderPicker(): LeaderPickerDialog

    @FragmentScoped
    @ContributesAndroidInjector(modules = [RenameDeckModule::class])
    internal abstract fun renameDeck(): RenameDeckDialog

    @FragmentScoped
    @ContributesAndroidInjector(modules = [FilterModule::class])
    internal abstract fun view(): FilterBottomSheetDialogFragment
}
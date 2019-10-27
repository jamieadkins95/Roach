package com.jamieadkins.gwent.deck.builder.di

import com.jamieadkins.gwent.di.FragmentScoped
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment
import com.jamieadkins.gwent.filter.FilterModule
import com.jamieadkins.gwent.deck.builder.DeckDetailsFragment
import com.jamieadkins.gwent.deck.builder.DeckDetailsModule
import com.jamieadkins.gwent.deck.builder.leader.LeaderPickerDialog
import com.jamieadkins.gwent.deck.builder.leader.LeaderPickerModule
import com.jamieadkins.gwent.deck.builder.rename.RenameDeckDialog
import com.jamieadkins.gwent.deck.builder.rename.RenameDeckModule
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
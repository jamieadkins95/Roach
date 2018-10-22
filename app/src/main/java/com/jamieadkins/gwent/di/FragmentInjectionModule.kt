package com.jamieadkins.gwent.di

import com.jamieadkins.gwent.deck.list.DeckListFragment
import com.jamieadkins.gwent.deck.list.DeckListModule
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentInjectionModule private constructor() {

    @FragmentScoped
    @ContributesAndroidInjector()
    internal abstract fun view(): FilterBottomSheetDialogFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [DeckListModule::class])
    internal abstract fun deckList(): DeckListFragment
}

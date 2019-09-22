package com.jamieadkins.gwent.di

import com.jamieadkins.gwent.card.detail.CardDetailModule
import com.jamieadkins.gwent.card.detail.CardDetailsFragment
import com.jamieadkins.gwent.card.list.CardDatabaseFragment
import com.jamieadkins.gwent.card.list.CardDatabaseModule
import com.jamieadkins.gwent.data.latest.DeckOfTheDayDataModule
import com.jamieadkins.gwent.data.latest.NewsDataModule
import com.jamieadkins.gwent.deck.create.CreateDeckDialog
import com.jamieadkins.gwent.deck.create.CreateDeckModule
import com.jamieadkins.gwent.deck.list.DeckListFragment
import com.jamieadkins.gwent.deck.list.DeckListModule
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment
import com.jamieadkins.gwent.filter.FilterModule
import com.jamieadkins.gwent.latest.GwentFragment
import com.jamieadkins.gwent.latest.GwentLatestModule
import com.jamieadkins.gwent.tracker.DeckTrackerSetupFragment
import com.jamieadkins.gwent.tracker.DeckTrackerSetupModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentInjectionModule private constructor() {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [FilterModule::class])
    internal abstract fun view(): FilterBottomSheetDialogFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [DeckListModule::class])
    internal abstract fun deckList(): DeckListFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [CreateDeckModule::class])
    internal abstract fun createDeck(): CreateDeckDialog

    @FragmentScoped
    @ContributesAndroidInjector(modules = [CardDetailModule::class])
    internal abstract fun detail(): CardDetailsFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [CardDatabaseModule::class])
    internal abstract fun cardDatabase(): CardDatabaseFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [GwentLatestModule::class, NewsDataModule::class, DeckOfTheDayDataModule::class])
    internal abstract fun gwent(): GwentFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [DeckTrackerSetupModule::class])
    internal abstract fun deckTrackerSetup(): DeckTrackerSetupFragment
}

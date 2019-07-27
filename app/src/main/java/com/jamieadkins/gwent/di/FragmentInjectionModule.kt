package com.jamieadkins.gwent.di

import com.jamieadkins.gwent.card.detail.CardDetailModule
import com.jamieadkins.gwent.card.detail.CardDetailsFragment
import com.jamieadkins.gwent.card.list.CardDatabaseFragment
import com.jamieadkins.gwent.card.list.CardDatabaseModule
import com.jamieadkins.gwent.deck.create.CreateDeckDialog
import com.jamieadkins.gwent.deck.create.CreateDeckModule
import com.jamieadkins.gwent.deck.detail.DeckDetailsFragment
import com.jamieadkins.gwent.deck.detail.DeckDetailsModule
import com.jamieadkins.gwent.deck.detail.leader.LeaderPickerDialog
import com.jamieadkins.gwent.deck.detail.leader.LeaderPickerModule
import com.jamieadkins.gwent.deck.detail.rename.RenameDeckDialog
import com.jamieadkins.gwent.deck.detail.rename.RenameDeckModule
import com.jamieadkins.gwent.deck.list.DeckListFragment
import com.jamieadkins.gwent.deck.list.DeckListModule
import com.jamieadkins.gwent.filter.FilterBottomSheetDialogFragment
import com.jamieadkins.gwent.filter.FilterModule
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
    @ContributesAndroidInjector(modules = [DeckDetailsModule::class])
    internal abstract fun deckBuilder(): DeckDetailsFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [LeaderPickerModule::class])
    internal abstract fun leaderPicker(): LeaderPickerDialog

    @FragmentScoped
    @ContributesAndroidInjector(modules = [RenameDeckModule::class])
    internal abstract fun renameDeck(): RenameDeckDialog

    @FragmentScoped
    @ContributesAndroidInjector(modules = [CardDetailModule::class])
    internal abstract fun detail(): CardDetailsFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [CardDatabaseModule::class])
    internal abstract fun cardDatabase(): CardDatabaseFragment
}

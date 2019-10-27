package com.jamieadkins.gwent.deck.builder.di

import com.jamieadkins.gwent.base.CoreComponent
import com.jamieadkins.gwent.card.data.CardDataModule
import com.jamieadkins.gwent.data.DataModule
import com.jamieadkins.gwent.data.deck.DeckDataModule
import com.jamieadkins.gwent.deck.builder.DeckDetailsActivity
import com.jamieadkins.gwent.di.ActivityScoped
import com.jamieadkins.gwent.di.FilterDataModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DeckBuilderFragmentInjectionModule::class,
        DataModule::class,
        CardDataModule::class,
        FilterDataModule::class,
        DeckDataModule::class
    ],
    dependencies = [CoreComponent::class]
)
@ActivityScoped
interface DeckBuilderComponent {

    fun inject(target: DeckDetailsActivity)

    @Component.Builder
    interface Builder {

        fun core(coreComponent: CoreComponent): Builder

        fun build(): DeckBuilderComponent
    }
}


package com.jamieadkins.gwent.decktracker

import com.jamieadkins.decktracker.data.DeckTrackerDataModule
import com.jamieadkins.decktracker.data.DeckTrackerScope
import com.jamieadkins.gwent.base.CoreComponent
import com.jamieadkins.gwent.card.data.CardDataModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DeckTrackerModule::class,
        DeckTrackerDataModule::class,
        CardDataModule::class
    ],
    dependencies = [CoreComponent::class]
)
@DeckTrackerScope
interface DeckTrackerComponent {

    fun inject(target: DeckTrackerActivity)

    @Component.Builder
    interface Builder {

        fun core(coreComponent: CoreComponent): Builder

        @BindsInstance
        fun activity(activity: DeckTrackerActivity): Builder

        fun build(): DeckTrackerComponent
    }
}
package com.jamieadkins.gwent.deckbuilder.trynow

import com.jamieadkins.gwent.base.CoreComponent
import com.jamieadkins.gwent.card.data.CardDataModule
import com.jamieadkins.gwent.data.DataModule
import com.jamieadkins.gwent.data.deck.DeckDataModule
import com.jamieadkins.gwent.data.update.UpdateDataModule
import com.jamieadkins.gwent.di.ActivityScoped
import com.jamieadkins.gwent.di.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        TryNowModule::class,
        UpdateDataModule::class,
        CardDataModule::class,
        DataModule::class,
        DeckDataModule::class,
        AppModule::class
    ],
    dependencies = [CoreComponent::class]
)
@ActivityScoped
interface TryNowComponent {

    fun inject(target: TryNowActivity)

    @Component.Builder
    interface Builder {

        fun core(coreComponent: CoreComponent): Builder

        @BindsInstance
        fun activity(activity: TryNowActivity): Builder

        fun build(): TryNowComponent
    }
}
package com.jamieadkins.gwent.di

import com.jamieadkins.gwent.base.CoreComponent
import com.jamieadkins.gwent.card.data.CardDataModule
import com.jamieadkins.gwent.card.detail.CardDetailsActivity
import com.jamieadkins.gwent.data.DataModule
import com.jamieadkins.gwent.data.deck.DeckDataModule
import com.jamieadkins.gwent.data.filter.FilterDataModule
import com.jamieadkins.gwent.data.update.UpdateDataModule
import com.jamieadkins.gwent.deck.detail.DeckDetailsActivity
import com.jamieadkins.gwent.main.MainActivity
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        FragmentInjectionModule::class,
        AppModule::class,
        DataModule::class,
        CardDataModule::class,
        FilterDataModule::class,
        UpdateDataModule::class,
        DeckDataModule::class
    ],
    dependencies = [CoreComponent::class]
)
@ActivityScoped
interface AppComponent {

    fun inject(target: MainActivity)
    fun inject(target: CardDetailsActivity)
    fun inject(target: DeckDetailsActivity)

    @Component.Builder
    interface Builder {

        fun core(coreComponent: CoreComponent): Builder

        fun build(): AppComponent
    }
}
package com.jamieadkins.gwent.di

import com.jamieadkins.gwent.card.list.CardDatabaseModule
import com.jamieadkins.gwent.data.DataModule
import com.jamieadkins.gwent.card.data.CardDataModule
import com.jamieadkins.gwent.data.deck.DeckDataModule
import com.jamieadkins.gwent.data.filter.FilterDataModule
import com.jamieadkins.gwent.data.update.UpdateDataModule
import com.jamieadkins.gwent.filter.FilterModule
import com.jamieadkins.gwent.launch.LaunchModule
import com.jamieadkins.gwent.main.GwentApplication
import com.jamieadkins.gwent.update.UpdateUiModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        FragmentInjectionModule::class,
        AppModule::class,
        DataModule::class,
        com.jamieadkins.gwent.card.data.CardDataModule::class,
        FilterDataModule::class,
        UpdateUiModule::class,
        UpdateDataModule::class,
        CardDatabaseModule::class,
        LaunchModule::class,
        FilterModule::class,
        DeckDataModule::class
    ]
)
@Singleton
abstract class AppComponent : AndroidInjector<GwentApplication> {

    @Component.Builder
    internal interface Builder {

        @BindsInstance
        fun application(application: GwentApplication): AppComponent.Builder

        fun build(): AppComponent
    }
}
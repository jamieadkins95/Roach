package com.jamieadkins.gwent.card.list

import com.jamieadkins.gwent.data.card.CardDataModule
import com.jamieadkins.gwent.data.update.UpdateDataModule
import com.jamieadkins.gwent.di.FragmentScoped
import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.card.CardDatabase
import com.jamieadkins.gwent.domain.card.GetCardsUseCase
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class CardDatabaseModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [UpdateDataModule::class])
    internal abstract fun view(): CardDatabaseFragment

    @FragmentScoped
    @Binds
    internal abstract fun view(activity: CardDatabaseFragment): CardDatabaseContract.View

    @FragmentScoped
    @Binds
    internal abstract fun presenter(presenter: CardDatabasePresenter): CardDatabaseContract.Presenter

    @Module
    companion object {

        @JvmStatic
        @Provides
        @Singleton
        fun usecase(cardRepository: CardRepository,
                    @CardDatabase filterRepository: FilterRepository,
                    schedulerProvider: SchedulerProvider): GetCardsUseCase {
            return GetCardsUseCase(cardRepository, filterRepository, schedulerProvider)
        }
    }
}
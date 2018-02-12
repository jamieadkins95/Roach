package com.jamieadkins.gwent

import android.content.Context
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.gwent.data.card.CardsInteractor
import com.jamieadkins.gwent.data.collection.CollectionInteractor
import com.jamieadkins.gwent.data.deck.DecksInteractor
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository

interface Injector {
    fun provideCardsInteractor(context: Context): CardsInteractor

    fun provideCollectionInteractor(): CollectionInteractor

    fun provideDecksInteractor(context: Context): DecksInteractor

    fun provideCardRepository(): CardRepository

    fun provideUpdateRepository(): UpdateRepository

    fun provideSchedulerProvider(): BaseSchedulerProvider
}
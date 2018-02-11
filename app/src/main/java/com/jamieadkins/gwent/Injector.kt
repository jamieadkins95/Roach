package com.jamieadkins.gwent

import android.content.Context
import com.jamieadkins.gwent.data.card.CardsInteractor
import com.jamieadkins.gwent.data.collection.CollectionInteractor
import com.jamieadkins.gwent.data.deck.DecksInteractor
import com.jamieadkins.gwent.data.repository.card.CardRepository

interface Injector {
    fun provideCardsInteractor(context: Context): CardsInteractor

    fun provideCollectionInteractor(): CollectionInteractor

    fun provideDecksInteractor(context: Context): DecksInteractor

    fun provideCardRepository(): CardRepository
}
package com.jamieadkins.gwent

import android.content.Context
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.CollectionInteractor
import com.jamieadkins.gwent.data.interactor.DecksInteractor

interface Injector {
    fun provideCardsInteractor(context: Context): CardsInteractor

    fun provideCollectionInteractor(): CollectionInteractor

    fun provideDecksInteractor(context: Context): DecksInteractor
}
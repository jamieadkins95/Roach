package com.jamieadkins.gwent

import android.content.Context
import com.jamieadkins.gwent.data.card.CachedCardsInteractor
import com.jamieadkins.gwent.data.card.CardsInteractor
import com.jamieadkins.gwent.data.collection.CollectionInteractor
import com.jamieadkins.gwent.data.collection.CollectionInteractorFirebase
import com.jamieadkins.gwent.data.deck.DecksInteractor
import com.jamieadkins.gwent.data.deck.DecksInteractorFirebase

object Injection : Injector {
    fun provideCardsInteractor(context: Context): CardsInteractor {
        return CachedCardsInteractor()
    }

    fun provideCollectionInteractor(): CollectionInteractor {
        return CollectionInteractorFirebase()
    }

    fun provideDecksInteractor(context: Context): DecksInteractor {
        val interactor: DecksInteractorFirebase = DecksInteractorFirebase()
        interactor.setCardsInteractor(provideCardsInteractor(context))
        return interactor
    }
}

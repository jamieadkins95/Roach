package com.jamieadkins.gwent

import android.content.Context
import android.support.v7.preference.PreferenceManager
import com.jamieadkins.gwent.data.interactor.*

object Injection : Injector {
    override fun provideCardsInteractor(context: Context): CardsInteractor {
        return CachedCardsInteractor()
    }

    override fun provideCollectionInteractor(): CollectionInteractor {
        return CollectionInteractorFirebase()
    }

    override fun provideDecksInteractor(context: Context): DecksInteractor {
        val interactor: DecksInteractorFirebase = DecksInteractorFirebase()
        interactor.setCardsInteractor(provideCardsInteractor(context))
        return interactor
    }
}

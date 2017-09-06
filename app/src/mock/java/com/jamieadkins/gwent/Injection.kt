package com.jamieadkins.gwent

import android.content.Context
import android.support.v7.preference.PreferenceManager
import com.jamieadkins.gwent.data.interactor.*

object Injection : Injector {
    override fun provideCardsInteractor(context: Context): CardsInteractor {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val locale = preferences.getString(context.getString(R.string.pref_locale_key), context.getString(R.string.default_locale))
        return CardsInteractorFirebase(locale)
    }

    override fun provideCollectionInteractor(): CollectionInteractor {
        return CollectionInteractorFirebase()
    }

    override fun provideDecksInteractor(context: Context): DecksInteractor {
        val interactor = DecksInteractorFirebase()
        interactor.setCardsInteractor(provideCardsInteractor(context))
        return interactor
    }
}
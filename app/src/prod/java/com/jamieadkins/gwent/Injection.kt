package com.jamieadkins.gwent

import android.content.Context
import android.support.v7.preference.PreferenceManager
import com.jamieadkins.gwent.data.interactor.CardsInteractor
import com.jamieadkins.gwent.data.interactor.CardsInteractorFirebase
import com.jamieadkins.gwent.data.interactor.CollectionInteractor
import com.jamieadkins.gwent.data.interactor.CollectionInteractorFirebase

object Injection {
    fun provideCardsInteractor(context: Context): CardsInteractor {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val locale = preferences.getString(context.getString(R.string.pref_locale_key), context.getString(R.string.default_locale))
        return CardsInteractorFirebase(locale)
    }

    fun provideCollectionInteractor(): CollectionInteractor {
        return CollectionInteractorFirebase()
    }
}

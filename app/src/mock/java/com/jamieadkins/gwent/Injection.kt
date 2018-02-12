package com.jamieadkins.gwent

import android.content.Context
import android.support.v7.preference.PreferenceManager
import com.jamieadkins.gwent.data.card.CachedCardsInteractor
import com.jamieadkins.gwent.data.card.CardsInteractor
import com.jamieadkins.gwent.data.card.CardsInteractorImpl
import com.jamieadkins.gwent.data.collection.CollectionInteractor
import com.jamieadkins.gwent.data.collection.CollectionInteractorFirebase
import com.jamieadkins.gwent.data.deck.DecksInteractor
import com.jamieadkins.gwent.data.deck.DecksInteractorFirebase
import com.jamieadkins.gwent.data.interactor.*
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.card.CardRepositoryImpl
import com.jamieadkins.gwent.data.repository.update.UpdateRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepositoryImpl

object Injection : Injector {
    override fun provideCardsInteractor(context: Context): CardsInteractor {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val locale = preferences.getString(context.getString(R.string.pref_locale_key), context.getString(R.string.default_locale))
        return CardsInteractorImpl()
    }

    override fun provideCollectionInteractor(): CollectionInteractor {
        return CollectionInteractorFirebase()
    }

    override fun provideDecksInteractor(context: Context): DecksInteractor {
        val interactor = DecksInteractorFirebase()
        interactor.setCardsInteractor(provideCardsInteractor(context))
        return interactor
    }

    override fun provideCardRepository(): CardRepository {
        return CardRepositoryImpl()
    }

    override fun provideUpdateRepository(): UpdateRepository {
        return UpdateRepositoryImpl()
    }
}
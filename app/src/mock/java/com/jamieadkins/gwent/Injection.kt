package com.jamieadkins.gwent

import android.content.Context
import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.SchedulerProvider
import com.jamieadkins.gwent.data.collection.CollectionInteractor
import com.jamieadkins.gwent.data.collection.CollectionInteractorFirebase
import com.jamieadkins.gwent.data.deck.DecksInteractor
import com.jamieadkins.gwent.data.deck.DecksInteractorFirebase
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.card.CardRepositoryImpl
import com.jamieadkins.gwent.data.repository.update.UpdateRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepositoryImpl
import com.jamieadkins.gwent.database.GwentDatabaseProvider
import com.jamieadkins.gwent.main.GwentApplication

object Injection : Injector {

    private val database by lazy { GwentDatabaseProvider.getDatabase(GwentApplication.INSTANCE.applicationContext) }

    override fun provideCollectionInteractor(): CollectionInteractor {
        return CollectionInteractorFirebase()
    }

    override fun provideDecksInteractor(context: Context): DecksInteractor {
        val interactor = DecksInteractorFirebase()
        interactor.setCardRepository(provideCardRepository())
        return interactor
    }

    override fun provideCardRepository(): CardRepository {
        return CardRepositoryImpl(database)
    }

    override fun provideUpdateRepository(): UpdateRepository {
        return UpdateRepositoryImpl(database)
    }

    override fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider
    }
}
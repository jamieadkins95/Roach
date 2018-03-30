package com.jamieadkins.gwent


import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.SchedulerProvider
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.data.repository.card.CardRepositoryImpl
import com.jamieadkins.gwent.data.repository.deck.DeckRepository
import com.jamieadkins.gwent.data.repository.deck.UserDeckRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepository
import com.jamieadkins.gwent.data.repository.update.UpdateRepositoryImpl
import com.jamieadkins.gwent.database.GwentDatabaseProvider
import com.jamieadkins.gwent.main.GwentApplication

object Injection {

    private val database by lazy { GwentDatabaseProvider.getDatabase(GwentApplication.INSTANCE.applicationContext) }


    fun provideDeckRepository(): DeckRepository {
        return UserDeckRepository(database)
    }

    fun provideCardRepository(): CardRepository {
        return CardRepositoryImpl(database)
    }

    fun provideUpdateRepository(): UpdateRepository {
        return UpdateRepositoryImpl(database)
    }

    fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider
    }
}
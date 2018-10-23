package com.jamieadkins.gwent.data.deck

import com.jamieadkins.gwent.data.deck.repository.UserDeckRepository
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DeckDataModule {

    @Binds
    @Singleton
    abstract fun repository(repository: UserDeckRepository): DeckRepository
}
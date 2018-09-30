package com.jamieadkins.gwent.data.deck

import com.jamieadkins.gwent.data.deck.repository.UserDeckRepository
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DeckDataModule {

    @Binds
    abstract fun repository(repository: UserDeckRepository): DeckRepository
}
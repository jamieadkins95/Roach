package com.jamieadkins.gwent.data.deck

import com.jamieadkins.gwent.data.deck.repository.UserDeckRepository
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
abstract class DeckDataModule {

    @Binds
    @Reusable
    abstract fun repository(repository: UserDeckRepository): DeckRepository
}
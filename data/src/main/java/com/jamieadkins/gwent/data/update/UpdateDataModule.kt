package com.jamieadkins.gwent.data.update

import com.jamieadkins.gwent.data.update.repository.CardUpdateRepository
import com.jamieadkins.gwent.data.update.repository.CategoryUpdateRepository
import com.jamieadkins.gwent.data.update.repository.KeywordUpdateRepository
import com.jamieadkins.gwent.data.update.repository.UpdateRepositoryImpl
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import dagger.Binds
import dagger.Module

@Module
abstract class UpdateDataModule {

    @Binds
    abstract fun repository(repository: UpdateRepositoryImpl): UpdateRepository

    @CardUpdate
    @Binds
    abstract fun card(repository: CardUpdateRepository): UpdateRepository

    @KeywordUpdate
    @Binds
    abstract fun keyword(repository: KeywordUpdateRepository): UpdateRepository

    @CategoryUpdate
    @Binds
    abstract fun category(repository: CategoryUpdateRepository): UpdateRepository
}
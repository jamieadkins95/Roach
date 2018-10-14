package com.jamieadkins.gwent.data.update

import com.jamieadkins.gwent.data.update.repository.CardUpdateRepository
import com.jamieadkins.gwent.data.update.repository.CategoryUpdateRepository
import com.jamieadkins.gwent.data.update.repository.KeywordUpdateRepository
import com.jamieadkins.gwent.data.update.repository.NotificationsRepository
import com.jamieadkins.gwent.data.update.repository.UpdateRepositoryImpl
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class UpdateDataModule {

    @Binds
    @Singleton
    abstract fun repository(repository: UpdateRepositoryImpl): UpdateRepository

    @CardUpdate
    @Binds
    @Singleton
    abstract fun card(repository: CardUpdateRepository): UpdateRepository

    @KeywordUpdate
    @Binds
    @Singleton
    abstract fun keyword(repository: KeywordUpdateRepository): UpdateRepository

    @CategoryUpdate
    @Binds
    @Singleton
    abstract fun category(repository: CategoryUpdateRepository): UpdateRepository

    @NotificationsUpdate
    @Binds
    @Singleton
    abstract fun notifications(repository: NotificationsRepository): UpdateRepository
}
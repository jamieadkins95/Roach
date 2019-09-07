package com.jamieadkins.gwent.data.update

import com.jamieadkins.gwent.data.update.repository.CardUpdateRepository
import com.jamieadkins.gwent.data.update.repository.CategoryUpdateRepository
import com.jamieadkins.gwent.data.update.repository.KeywordUpdateRepository
import com.jamieadkins.gwent.data.update.repository.NoticesRepositoryImpl
import com.jamieadkins.gwent.data.update.repository.NotificationsRepository
import com.jamieadkins.gwent.data.update.repository.UpdateRepositoryImpl
import com.jamieadkins.gwent.domain.update.repository.NoticesRepository
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module(includes = [PatchDataModule::class])
abstract class UpdateDataModule {

    @Binds
    @Reusable
    abstract fun repository(repository: UpdateRepositoryImpl): UpdateRepository

    @CardUpdate
    @Binds
    @Reusable
    abstract fun card(repository: CardUpdateRepository): UpdateRepository

    @KeywordUpdate
    @Binds
    @Reusable
    abstract fun keyword(repository: KeywordUpdateRepository): UpdateRepository

    @CategoryUpdate
    @Binds
    @Reusable
    abstract fun category(repository: CategoryUpdateRepository): UpdateRepository

    @NotificationsUpdate
    @Binds
    @Reusable
    abstract fun notifications(repository: NotificationsRepository): UpdateRepository

    @Binds
    @Reusable
    abstract fun notices(repository: NoticesRepositoryImpl): NoticesRepository
}
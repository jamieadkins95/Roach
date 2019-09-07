package com.jamieadkins.gwent.data.update

import com.jamieadkins.gwent.data.update.repository.PatchRepositoryImpl
import com.jamieadkins.gwent.domain.patch.PatchRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
abstract class PatchDataModule {

    @Binds
    @Reusable
    abstract fun repository(repository: PatchRepositoryImpl): PatchRepository

}
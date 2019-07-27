package com.jamieadkins.gwent.data.filter

import com.jamieadkins.gwent.data.filter.repository.FilterRepositoryImpl
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
abstract class FilterDataModule {

    @Binds
    @Reusable
    abstract fun filter(repository: FilterRepositoryImpl): FilterRepository
}
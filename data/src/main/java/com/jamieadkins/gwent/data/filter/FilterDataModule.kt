package com.jamieadkins.gwent.data.filter

import com.jamieadkins.gwent.data.filter.repository.FilterRepositoryImpl
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class FilterDataModule {

    @Binds
    @Singleton
    abstract fun filter(repository: FilterRepositoryImpl): FilterRepository
}
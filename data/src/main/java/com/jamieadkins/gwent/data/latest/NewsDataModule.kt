package com.jamieadkins.gwent.data.latest

import com.jamieadkins.gwent.domain.latest.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
abstract class NewsDataModule {

    @Binds
    @Reusable
    abstract fun repository(repository: NewsRepositoryImpl): NewsRepository

}
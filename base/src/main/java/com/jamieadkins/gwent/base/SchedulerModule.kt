package com.jamieadkins.gwent.base

import com.jamieadkins.gwent.domain.SchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class SchedulerModule {

    @Binds
    abstract fun schedulerProvider(scheduler: SchedulerProviderImpl): SchedulerProvider
}
package com.jamieadkins.gwent.domain.update.repository

import com.jamieadkins.gwent.domain.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class IsInstantAppUseCase @Inject constructor(
    private val repository: InstantAppsRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun isInstantApp(): Single<Boolean> {
        return repository.isInstantApp()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
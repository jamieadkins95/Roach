package com.jamieadkins.gwent.domain.update.repository

import com.jamieadkins.gwent.domain.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class StartCardDatabaseUpdateUseCase @Inject constructor(
    private val updateRepository: UpdateRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun start(): Completable {
        return updateRepository.performUpdate()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
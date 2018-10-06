package com.jamieadkins.gwent.domain.update.repository

import com.jamieadkins.gwent.domain.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

class GetCardDatabaseUpdateUseCase @Inject constructor(
    private val updateRepository: UpdateRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun isUpdateAvailable(): Observable<Boolean> {
        return updateRepository.isUpdateAvailable()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
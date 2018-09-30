package com.jamieadkins.gwent.domain.update.repository

import com.jamieadkins.gwent.domain.SchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class GetCardDatabaseUpdateUseCase @Inject constructor(
    private val updateRepository: UpdateRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun isUpdateAvailable(): Single<Boolean> {
        return updateRepository.isUpdateAvailable()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
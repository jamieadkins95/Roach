package com.jamieadkins.gwent.domain.launch

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.update.repository.UpdateRepository
import io.reactivex.Completable
import javax.inject.Inject

class PerformFirstTimeSetupUseCase @Inject constructor(
    private val updateRepository: UpdateRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun performFirstTimeSetup(): Completable {
        return updateRepository.hasDoneFirstTimeSetup()
            .firstOrError()
            .flatMapCompletable { doneSetup ->
                if (!doneSetup) {
                    updateRepository.performFirstTimeSetup()
                } else {
                    Completable.complete()
                }
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
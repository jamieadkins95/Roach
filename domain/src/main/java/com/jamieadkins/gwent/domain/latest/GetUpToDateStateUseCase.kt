package com.jamieadkins.gwent.domain.latest

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.patch.GwentPatch
import com.jamieadkins.gwent.domain.patch.PatchRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetUpToDateStateUseCase @Inject constructor(
    private val patchRepository: PatchRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun getUpToDateState(): Observable<GwentPatch> = patchRepository.getLatestGwentPatch()
        .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
}
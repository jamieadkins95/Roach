package com.jamieadkins.gwent.domain.update.repository

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.update.model.Notice
import io.reactivex.Observable
import javax.inject.Inject

class GetNoticesUseCase @Inject constructor(
    private val noticesRepository: NoticesRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun getNotices(): Observable<List<Notice>> {
        return noticesRepository.getNotices()
            .map { it.filter { notice -> notice.enabled } }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
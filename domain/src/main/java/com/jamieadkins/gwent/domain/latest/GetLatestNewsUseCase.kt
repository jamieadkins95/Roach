package com.jamieadkins.gwent.domain.latest

import com.jamieadkins.gwent.domain.LocaleRepository
import com.jamieadkins.gwent.domain.SchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

class GetLatestNewsUseCase @Inject constructor(
    private val localeRepository: LocaleRepository,
    private val newsRepository: NewsRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun getLatestNews(): Observable<List<GwentNewsArticle>> {
        return localeRepository.getNewsLocale()
            .switchMap(newsRepository::getLatestNews)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }
}
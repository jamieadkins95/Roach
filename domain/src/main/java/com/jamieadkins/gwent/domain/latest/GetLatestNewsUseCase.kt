package com.jamieadkins.gwent.domain.latest

import com.jamieadkins.gwent.domain.LocaleRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetLatestNewsUseCase @Inject constructor(
    private val localeRepository: LocaleRepository,
    private val newsRepository: NewsRepository
) {

    fun getLatestNews(): Observable<List<GwentNewsArticle>> = localeRepository.getNewsLocale().switchMap(newsRepository::getLatestNews)
}
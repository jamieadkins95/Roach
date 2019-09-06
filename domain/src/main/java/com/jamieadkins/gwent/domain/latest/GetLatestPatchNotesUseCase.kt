package com.jamieadkins.gwent.domain.latest

import com.jamieadkins.gwent.domain.LocaleRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetLatestPatchNotesUseCase @Inject constructor(
    private val localeRepository: LocaleRepository,
    private val newsRepository: NewsRepository
) {

    fun getPatchNotes(): Observable<GwentNewsArticle> = localeRepository.getNewsLocale().switchMap(newsRepository::getLatestPatchNotes)
}
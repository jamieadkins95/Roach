package com.jamieadkins.gwent.domain.latest

import io.reactivex.Observable

interface NewsRepository {

    fun getLatestPatchNotes(locale: String): Observable<GwentNewsArticle>

    fun getLatestNews(locale: String): Observable<List<GwentNewsArticle>>
}
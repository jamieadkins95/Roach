package com.jamieadkins.gwent.domain

import io.reactivex.Observable

interface LocaleRepository {

    fun getLocale(): Observable<String>

    fun getNewsLocale(): Observable<String>

    fun getDefaultLocale(): Observable<String>
}
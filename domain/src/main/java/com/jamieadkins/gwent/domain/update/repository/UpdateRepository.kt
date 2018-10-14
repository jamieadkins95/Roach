package com.jamieadkins.gwent.domain.update.repository

import io.reactivex.Completable
import io.reactivex.Observable

interface UpdateRepository {

    fun isUpdateAvailable(): Observable<Boolean>

    fun performUpdate(): Completable

    fun hasDoneFirstTimeSetup(): Observable<Boolean>

    fun performFirstTimeSetup(): Completable
}
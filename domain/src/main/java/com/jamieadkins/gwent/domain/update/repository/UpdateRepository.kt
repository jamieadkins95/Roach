package com.jamieadkins.gwent.domain.update.repository

import com.jamieadkins.gwent.domain.update.model.UpdateResult
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

interface UpdateRepository {

    fun isUpdateAvailable(): Single<Boolean>

    fun performFirstTimeSetup(): Observable<UpdateResult>

    fun performUpdate(): Completable

    fun getNewCardData(): Single<File>
}
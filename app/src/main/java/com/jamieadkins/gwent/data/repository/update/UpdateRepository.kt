package com.jamieadkins.gwent.data.repository.update

import io.reactivex.Completable
import io.reactivex.Single
import java.io.File

interface UpdateRepository {

    fun isUpdateAvailable(): Single<Boolean>

    fun performUpdate(): Completable

    fun getNewCardData(): Single<File>
}
package com.jamieadkins.gwent.domain.update.repository

import io.reactivex.Single

interface InstantAppsRepository {

    fun isInstantApp(): Single<Boolean>
}
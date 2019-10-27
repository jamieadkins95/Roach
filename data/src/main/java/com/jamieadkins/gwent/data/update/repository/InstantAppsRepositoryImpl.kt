package com.jamieadkins.gwent.data.update.repository

import android.content.Context
import com.google.android.instantapps.InstantApps
import com.jamieadkins.gwent.domain.update.repository.InstantAppsRepository
import io.reactivex.Single
import javax.inject.Inject

class InstantAppsRepositoryImpl @Inject constructor(
    private val context: Context
) : InstantAppsRepository {

    override fun isInstantApp(): Single<Boolean> {
        return Single.fromCallable { InstantApps.isInstantApp(context) }
    }
}
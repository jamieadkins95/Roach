package com.jamieadkins.gwent.domain.patch

import io.reactivex.Observable
import io.reactivex.Single

interface PatchRepository {

    fun getLatestRoachPatch(): Single<String>

    fun getLatestGwentPatch(): Observable<GwentPatch>
}